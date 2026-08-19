package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.CategoriaMovimientoEntity;
import com.example.SistemaCaja.entities.CuentaCajaEntity;
import com.example.SistemaCaja.entities.MovimientoCajaEntity;
import com.example.SistemaCaja.models.MovimientoCaja;
import com.example.SistemaCaja.repositories.ICategoriaMovimientoRepository;
import com.example.SistemaCaja.repositories.ICuentaCajaRepository;
import com.example.SistemaCaja.repositories.IMovimientoCajaRepository;
import com.example.SistemaCaja.services.IMovimientoCajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public MovimientoCaja guardar(MovimientoCaja movimientoCaja) {
        MovimientoCajaEntity entity = convertirAEntity(movimientoCaja);
        return convertirAModel(movimientoCajaRepository.save(entity));
    }

    @Override
    public void eliminar(Long id) {
        movimientoCajaRepository.deleteById(id);
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
