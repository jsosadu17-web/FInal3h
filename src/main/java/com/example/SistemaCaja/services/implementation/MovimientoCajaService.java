package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.CategoriaMovimientoEntity;
import com.example.SistemaCaja.entities.CuentaCajaEntity;
import com.example.SistemaCaja.entities.MovimientoCajaEntity;
import com.example.SistemaCaja.models.MovimientoCaja;
import com.example.SistemaCaja.exceptions.ReglaNegocioException;
import com.example.SistemaCaja.repositories.ICategoriaMovimientoRepository;
import com.example.SistemaCaja.repositories.ICuentaCajaRepository;
import com.example.SistemaCaja.repositories.IMovimientoCajaRepository;
import com.example.SistemaCaja.services.IMovimientoCajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoCajaService implements IMovimientoCajaService {

    private final IMovimientoCajaRepository movimientoCajaRepository;
    private final ICuentaCajaRepository cuentaCajaRepository;
    private final ICategoriaMovimientoRepository categoriaMovimientoRepository;

    @Override
    public List<MovimientoCaja> obtenerTodos(LocalDate fechaInicio, LocalDate fechaFin, String tipo, Long cuentaCajaId) {
        LocalDateTime inicio = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime fin = fechaFin != null ? fechaFin.atTime(LocalTime.MAX) : null;
        String tipoNormalizado = tipo == null || tipo.isBlank() ? null : tipo;

        return movimientoCajaRepository.buscarFiltrado(inicio, fin, tipoNormalizado, cuentaCajaId)
                .stream()
                .map(this::convertirAModel)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoCaja obtenerPorId(Long id) {
        return movimientoCajaRepository.findById(id)
                .map(this::convertirAModel)
                .orElse(null);
    }

    @Override
    @Transactional
    public MovimientoCaja guardar(MovimientoCaja movimientoCaja) {
        if (movimientoCaja.getId() != null) {
            MovimientoCajaEntity movimientoAnterior = movimientoCajaRepository.findById(movimientoCaja.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El movimiento de caja no existe"));
            revertirSaldo(movimientoAnterior);
        }

        MovimientoCajaEntity entity = convertirAEntity(movimientoCaja);
        aplicarSaldo(entity);
        return convertirAModel(movimientoCajaRepository.save(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        MovimientoCajaEntity movimiento = movimientoCajaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El movimiento de caja no existe"));
        revertirSaldo(movimiento);
        movimientoCajaRepository.delete(movimiento);
    }

    private void aplicarSaldo(MovimientoCajaEntity movimiento) {
        ajustarSaldo(movimiento.getCuentaCaja(), movimiento.getTipo(), movimiento.getMonto());
    }

    private void revertirSaldo(MovimientoCajaEntity movimiento) {
        String tipoInverso = "INGRESO".equals(movimiento.getTipo()) ? "EGRESO" : "INGRESO";
        ajustarSaldo(movimiento.getCuentaCaja(), tipoInverso, movimiento.getMonto());
    }

    private void ajustarSaldo(CuentaCajaEntity cuentaCaja, String tipo, BigDecimal monto) {
        double saldoActual = cuentaCaja.getSaldo() == null ? 0D : cuentaCaja.getSaldo();
        double montoMovimiento = monto.doubleValue();
        double nuevoSaldo = "INGRESO".equals(tipo)
                ? saldoActual + montoMovimiento
                : saldoActual - montoMovimiento;

        if (nuevoSaldo < 0) {
            throw new ReglaNegocioException("El egreso supera el saldo disponible de la cuenta de caja");
        }

        cuentaCaja.setSaldo(nuevoSaldo);
        cuentaCajaRepository.save(cuentaCaja);
    }

    private MovimientoCaja convertirAModel(MovimientoCajaEntity entity) {
        if (entity == null) return null;
        return MovimientoCaja.builder()
                .id(entity.getId())
                .fechaMovimiento(entity.getFechaMovimiento())
                .tipo(entity.getTipo())
                .monto(entity.getMonto())
                .descripcion(entity.getDescripcion())
                .cuentaCajaId(entity.getCuentaCaja().getId())
                .categoriaMovimientoId(entity.getCategoriaMovimiento().getId())
                .nombreCuenta(entity.getCuentaCaja().getNombreCuenta())
                .nombreCategoria(entity.getCategoriaMovimiento().getNombre())
                .build();
    }

    private MovimientoCajaEntity convertirAEntity(MovimientoCaja model) {
        if (model == null) return null;
        CuentaCajaEntity cuentaCaja = cuentaCajaRepository.findById(model.getCuentaCajaId())
                .orElseThrow(() -> new IllegalArgumentException("La cuenta de caja no existe"));
        CategoriaMovimientoEntity categoriaMovimiento = categoriaMovimientoRepository.findById(model.getCategoriaMovimientoId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría de movimiento no existe"));

        return MovimientoCajaEntity.builder()
                .id(model.getId())
                .fechaMovimiento(model.getFechaMovimiento())
                .tipo(model.getTipo())
                .monto(model.getMonto())
                .descripcion(model.getDescripcion())
                .cuentaCaja(cuentaCaja)
                .categoriaMovimiento(categoriaMovimiento)
                .build();
    }
}
