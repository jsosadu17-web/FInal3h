package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.dto.DashboardConsolidadoResponse;
import com.example.SistemaCaja.dto.IndicadorLiquidezResponse;
import com.example.SistemaCaja.entities.EmpresaEntity;
import com.example.SistemaCaja.entities.IndicadorLiquidezEntity;
import com.example.SistemaCaja.entities.MovimientoCajaEntity;
import com.example.SistemaCaja.entities.ProyeccionFlujoCajaEntity;
import com.example.SistemaCaja.exceptions.RecursoNoEncontradoException;
import com.example.SistemaCaja.repositories.IEmpresaRepository;
import com.example.SistemaCaja.repositories.IIndicadorLiquidezRepository;
import com.example.SistemaCaja.repositories.IMovimientoCajaRepository;
import com.example.SistemaCaja.repositories.IProyeccionFlujoCajaRepository;
import com.example.SistemaCaja.services.IIndicadorLiquidezService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndicadorLiquidezService implements IIndicadorLiquidezService {

    private final IIndicadorLiquidezRepository indicadorRepository;
    private final IEmpresaRepository empresaRepository;
    private final IMovimientoCajaRepository movimientoCajaRepository;
    private final IProyeccionFlujoCajaRepository proyeccionRepository;

    @Override
    public IndicadorLiquidezResponse calcularYGuardarIndicador(Long empresaId, String periodo) {
        EmpresaEntity empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

        List<ProyeccionFlujoCajaEntity> proyecciones = proyeccionRepository.findByEmpresaIdOrderByFechaGeneracionDesc(empresaId);

        BigDecimal ingresosProyectados = proyecciones.stream()
                .map(ProyeccionFlujoCajaEntity::getIngresosProyectados)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal egresosProyectados = proyecciones.stream()
                .map(ProyeccionFlujoCajaEntity::getEgresosProyectados)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal capitalTrabajo = ingresosProyectados.subtract(egresosProyectados);

        BigDecimal razonCorriente = egresosProyectados.compareTo(BigDecimal.ZERO) > 0
                ? ingresosProyectados.divide(egresosProyectados, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Cálculo seguro de días de caja sin riesgo de división por cero
        int diasCaja = 0;
        if (egresosProyectados.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal egresoDiario = egresosProyectados.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP);
            if (egresoDiario.compareTo(BigDecimal.ZERO) > 0) {
                diasCaja = capitalTrabajo.divide(egresoDiario, 0, RoundingMode.DOWN).intValue();
            }
        }

        String nivelRiesgo;
        if (razonCorriente.compareTo(new BigDecimal("1.5")) >= 0) {
            nivelRiesgo = "BAJO";
        } else if (razonCorriente.compareTo(BigDecimal.ONE) >= 0) {
            nivelRiesgo = "MEDIO";
        } else {
            nivelRiesgo = "ALTO";
        }

        IndicadorLiquidezEntity entidad = indicadorRepository.findByEmpresaIdAndPeriodo(empresaId, periodo)
                .orElseGet(IndicadorLiquidezEntity::new);

        entidad.setEmpresa(empresa);
        entidad.setPeriodo(periodo);
        entidad.setCapitalTrabajo(capitalTrabajo);
        entidad.setRazonCorriente(razonCorriente);
        entidad.setDiasCajaDisponible(diasCaja);
        entidad.setNivelRiesgo(nivelRiesgo);
        entidad.setFechaCalculo(LocalDateTime.now());

        IndicadorLiquidezEntity guardado = indicadorRepository.save(entidad);
        return mapearAResponse(guardado);
    }

    @Override
    public IndicadorLiquidezResponse obtenerPorEmpresaYPeriodo(Long empresaId, String periodo) {
        IndicadorLiquidezEntity entidad = indicadorRepository.findByEmpresaIdAndPeriodo(empresaId, periodo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay indicadores registrados para el periodo: " + periodo));
        return mapearAResponse(entidad);
    }

    @Override
    public DashboardConsolidadoResponse obtenerDashboardConsolidado(Long empresaId) {
        EmpresaEntity empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));


        List<MovimientoCajaEntity> movimientos = movimientoCajaRepository.findAll().stream()
                .filter(m -> m.getCuentaCaja() != null
                        && m.getCuentaCaja().getEmpresa() != null
                        && m.getCuentaCaja().getEmpresa().getId().equals(empresaId))
                .collect(Collectors.toList());

        List<ProyeccionFlujoCajaEntity> proyecciones = proyeccionRepository.findByEmpresaIdOrderByFechaGeneracionDesc(empresaId);
        List<IndicadorLiquidezEntity> historial = indicadorRepository.findByEmpresaId(empresaId);

        BigDecimal ingresosReales = movimientos.stream()
                .filter(m -> "INGRESO".equalsIgnoreCase(m.getTipo()))
                .map(MovimientoCajaEntity::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal egresosReales = movimientos.stream()
                .filter(m -> "EGRESO".equalsIgnoreCase(m.getTipo()))
                .map(MovimientoCajaEntity::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ingresosProyectados = proyecciones.stream()
                .map(ProyeccionFlujoCajaEntity::getIngresosProyectados)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal egresosProyectados = proyecciones.stream()
                .map(ProyeccionFlujoCajaEntity::getEgresosProyectados)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<IndicadorLiquidezResponse> historialDTO = historial.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        IndicadorLiquidezResponse ultimoIndicador = historialDTO.isEmpty() ? null : historialDTO.get(historialDTO.size() - 1);

        return DashboardConsolidadoResponse.builder()
                .empresaId(empresa.getId())
                .nombreEmpresa(empresa.getRazonSocial())
                .totalIngresosReales(ingresosReales)
                .totalEgresosReales(egresosReales)
                .saldoCajaActual(ingresosReales.subtract(egresosReales))
                .totalIngresosProyectados(ingresosProyectados)
                .totalEgresosProyectados(egresosProyectados)
                .ultimoIndicador(ultimoIndicador)
                .historialIndicadores(historialDTO)
                .build();
    }

    private IndicadorLiquidezResponse mapearAResponse(IndicadorLiquidezEntity e) {
        return IndicadorLiquidezResponse.builder()
                .id(e.getId())
                .empresaId(e.getEmpresa().getId())
                .nombreEmpresa(e.getEmpresa().getRazonSocial())
                .periodo(e.getPeriodo())
                .capitalTrabajo(e.getCapitalTrabajo())
                .razonCorriente(e.getRazonCorriente())
                .diasCajaDisponible(e.getDiasCajaDisponible())
                .nivelRiesgo(e.getNivelRiesgo())
                .fechaCalculo(e.getFechaCalculo())
                .build();
    }
}