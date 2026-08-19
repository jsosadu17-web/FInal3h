package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.dto.*;
import com.example.SistemaCaja.entities.*;
import com.example.SistemaCaja.exceptions.*;
import com.example.SistemaCaja.repositories.*;
import com.example.SistemaCaja.services.ISimulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SimulacionService implements ISimulacionService {
    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private final IEscenarioRepository escenarioRepository;
    private final IEmpresaRepository empresaRepository;
    private final ICuentaCajaRepository cuentaCajaRepository;
    private final IMovimientoCajaRepository movimientoCajaRepository;
    private final IProyeccionFlujoCajaRepository proyeccionRepository;

    @Override @Transactional
    public SimulacionResponse ejecutar(SimulacionRequest request) {
        if (request.getFechaInicioHistorial().isAfter(request.getFechaFinHistorial()))
            throw new ReglaNegocioException("El rango histórico es inválido");
        EscenarioEntity escenario = escenarioRepository.findById(request.getEscenarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El escenario no existe: " + request.getEscenarioId()));
        if (!Boolean.TRUE.equals(escenario.getActivo())) throw new ReglaNegocioException("El escenario está inactivo");
        Long empresaId = escenario.getEmpresa().getId();
        EmpresaEntity empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La empresa no existe: " + empresaId));
        if (cuentaCajaRepository.buscarPorEmpresaIdNative(empresaId).isEmpty())
            throw new ReglaNegocioException("La empresa no tiene cuentas de caja");
        LocalDateTime inicio = request.getFechaInicioHistorial().atStartOfDay();
        LocalDateTime fin = request.getFechaFinHistorial().atTime(LocalTime.MAX);
        List<MovimientoCajaEntity> movimientos = movimientoCajaRepository.buscarPorEmpresaYPeriodo(empresaId, inicio, fin);
        if (movimientos.isEmpty()) throw new ReglaNegocioException("La empresa no tiene movimientos históricos en el rango");
        int periodosHistoricos = periodosHistoricos(escenario.getTipoPeriodo(), request.getFechaInicioHistorial(), request.getFechaFinHistorial());
        BigDecimal ingresos = sum(movimientos, "INGRESO");
        BigDecimal egresos = sum(movimientos, "EGRESO");
        BigDecimal ingresoBase = money(ingresos.divide(BigDecimal.valueOf(periodosHistoricos), MC));
        BigDecimal egresoBase = money(egresos.divide(BigDecimal.valueOf(periodosHistoricos), MC));
        BigDecimal ingresoProyectado = money(ingresoBase.multiply(BigDecimal.ONE.add(escenario.getVariacionIngresosPorcentaje().divide(CIEN, MC)), MC));
        BigDecimal egresoProyectado = money(egresoBase.multiply(BigDecimal.ONE.add(escenario.getVariacionEgresosPorcentaje().divide(CIEN, MC)), MC));
        BigDecimal saldo = money(BigDecimal.valueOf(Optional.ofNullable(cuentaCajaRepository.sumarSaldoPorEmpresa(empresaId)).orElse(0D)));
        LocalDate fechaInicio = request.getFechaFinHistorial().plusDays(1);
        LocalDateTime generado = LocalDateTime.now();
        List<ProyeccionFlujoCajaEntity> proyecciones = new ArrayList<>();
        for (int i = 1; i <= escenario.getNumeroPeriodos(); i++) {
            LocalDate fechaFin = escenario.getTipoPeriodo() == TipoPeriodo.MENSUAL
                    ? fechaInicio.plusMonths(1).minusDays(1) : fechaInicio.plusDays(6);
            BigDecimal flujo = money(ingresoProyectado.subtract(egresoProyectado));
            BigDecimal saldoInicial = saldo;
            saldo = money(saldo.add(flujo));
            proyecciones.add(ProyeccionFlujoCajaEntity.builder().escenario(escenario).empresa(empresa).numeroPeriodo(i)
                    .fechaInicio(fechaInicio).fechaFin(fechaFin).ingresosBase(ingresoBase).egresosBase(egresoBase)
                    .ingresosProyectados(ingresoProyectado).egresosProyectados(egresoProyectado).flujoNetoProyectado(flujo)
                    .saldoInicial(saldoInicial).saldoFinalProyectado(saldo).fechaGeneracion(generado).build());
            fechaInicio = fechaFin.plusDays(1);
        }
        if (Boolean.TRUE.equals(request.getGuardarResultado())) {
            proyeccionRepository.deleteByEscenarioId(escenario.getId());
            proyeccionRepository.saveAll(proyecciones);
        }
        return SimulacionResponse.builder().escenarioId(escenario.getId()).empresaId(empresaId).nombreEscenario(escenario.getNombre())
                .tipoPeriodo(escenario.getTipoPeriodo()).fechaInicioHistorial(request.getFechaInicioHistorial())
                .fechaFinHistorial(request.getFechaFinHistorial()).cantidadPeriodosHistoricos(periodosHistoricos)
                .saldoInicialTotal(money(BigDecimal.valueOf(Optional.ofNullable(cuentaCajaRepository.sumarSaldoPorEmpresa(empresaId)).orElse(0D))))
                .totalPeriodosProyectados(proyecciones.size()).fechaGeneracion(generado)
                .proyecciones(proyecciones.stream().map(this::toResponse).toList()).build();
    }

    @Override @Transactional(readOnly = true)
    public List<ProyeccionResponse> listarPorEscenario(Long escenarioId) {
        if (!escenarioRepository.existsById(escenarioId)) throw new RecursoNoEncontradoException("El escenario no existe: " + escenarioId);
        return proyeccionRepository.findByEscenarioIdOrderByNumeroPeriodoAsc(escenarioId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<ProyeccionResponse> listarPorEmpresa(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) throw new RecursoNoEncontradoException("La empresa no existe: " + empresaId);
        return proyeccionRepository.findByEmpresaIdOrderByFechaGeneracionDesc(empresaId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public void eliminarPorEscenario(Long escenarioId) {
        if (!escenarioRepository.existsById(escenarioId)) throw new RecursoNoEncontradoException("El escenario no existe: " + escenarioId);
        proyeccionRepository.deleteByEscenarioId(escenarioId);
    }

    private int periodosHistoricos(TipoPeriodo tipo, LocalDate inicio, LocalDate fin) {
        if (tipo == TipoPeriodo.MENSUAL) return Math.max(1, (int) ChronoUnit.MONTHS.between(YearMonth.from(inicio), YearMonth.from(fin)) + 1);
        return Math.max(1, (int) ChronoUnit.WEEKS.between(inicio, fin) + 1);
    }

    private BigDecimal sum(List<MovimientoCajaEntity> movimientos, String tipo) {
        return movimientos.stream().filter(m -> tipo.equalsIgnoreCase(m.getTipo())).map(MovimientoCajaEntity::getMonto)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, (a, b) -> a.add(b, MC));
    }

    private BigDecimal money(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }

    private ProyeccionResponse toResponse(ProyeccionFlujoCajaEntity p) {
        return ProyeccionResponse.builder().numeroPeriodo(p.getNumeroPeriodo()).fechaInicio(p.getFechaInicio()).fechaFin(p.getFechaFin())
                .ingresosBase(p.getIngresosBase()).egresosBase(p.getEgresosBase()).ingresosProyectados(p.getIngresosProyectados())
                .egresosProyectados(p.getEgresosProyectados()).flujoNetoProyectado(p.getFlujoNetoProyectado())
                .saldoInicial(p.getSaldoInicial()).saldoFinalProyectado(p.getSaldoFinalProyectado()).build();
    }
}
