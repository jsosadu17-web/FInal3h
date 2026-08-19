package com.example.SistemaCaja;

import com.example.SistemaCaja.entities.CategoriaMovimientoEntity;
import com.example.SistemaCaja.entities.CuentaCajaEntity;
import com.example.SistemaCaja.entities.MovimientoCajaEntity;
import com.example.SistemaCaja.exceptions.ReglaNegocioException;
import com.example.SistemaCaja.models.MovimientoCaja;
import com.example.SistemaCaja.repositories.ICategoriaMovimientoRepository;
import com.example.SistemaCaja.repositories.ICuentaCajaRepository;
import com.example.SistemaCaja.repositories.IMovimientoCajaRepository;
import com.example.SistemaCaja.services.implementation.MovimientoCajaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoCajaServiceTest {

    @Mock IMovimientoCajaRepository movimientoCajaRepository;
    @Mock ICuentaCajaRepository cuentaCajaRepository;
    @Mock ICategoriaMovimientoRepository categoriaMovimientoRepository;
    @InjectMocks MovimientoCajaService service;

    @Test
    void sumaSaldoCuandoRegistraIngreso() {
        CuentaCajaEntity cuenta = CuentaCajaEntity.builder().id(1L).nombreCuenta("Caja").saldo(100D).build();
        CategoriaMovimientoEntity categoria = CategoriaMovimientoEntity.builder().id(2L).nombre("Ventas").build();
        MovimientoCajaEntity guardado = MovimientoCajaEntity.builder().id(10L).fechaMovimiento(LocalDateTime.now())
                .tipo("INGRESO").monto(new BigDecimal("50.00")).descripcion("Venta")
                .cuentaCaja(cuenta).categoriaMovimiento(categoria).build();

        when(cuentaCajaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(categoriaMovimientoRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(movimientoCajaRepository.save(any(MovimientoCajaEntity.class))).thenReturn(guardado);

        service.guardar(movimiento("INGRESO", "50.00"));

        assertEquals(150D, cuenta.getSaldo());
        verify(cuentaCajaRepository).save(cuenta);
    }

    @Test
    void restaSaldoCuandoRegistraEgreso() {
        CuentaCajaEntity cuenta = CuentaCajaEntity.builder().id(1L).nombreCuenta("Caja").saldo(100D).build();
        CategoriaMovimientoEntity categoria = CategoriaMovimientoEntity.builder().id(2L).nombre("Compras").build();
        MovimientoCajaEntity guardado = MovimientoCajaEntity.builder().id(10L).fechaMovimiento(LocalDateTime.now())
                .tipo("EGRESO").monto(new BigDecimal("30.00")).descripcion("Compra")
                .cuentaCaja(cuenta).categoriaMovimiento(categoria).build();

        when(cuentaCajaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(categoriaMovimientoRepository.findById(2L)).thenReturn(Optional.of(categoria));
        when(movimientoCajaRepository.save(any(MovimientoCajaEntity.class))).thenReturn(guardado);

        service.guardar(movimiento("EGRESO", "30.00"));

        assertEquals(70D, cuenta.getSaldo());
        verify(cuentaCajaRepository).save(cuenta);
    }

    @Test
    void rechazaEgresoMayorAlSaldoDisponible() {
        CuentaCajaEntity cuenta = CuentaCajaEntity.builder().id(1L).nombreCuenta("Caja").saldo(20D).build();
        CategoriaMovimientoEntity categoria = CategoriaMovimientoEntity.builder().id(2L).nombre("Compras").build();

        when(cuentaCajaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(categoriaMovimientoRepository.findById(2L)).thenReturn(Optional.of(categoria));

        assertThrows(ReglaNegocioException.class, () -> service.guardar(movimiento("EGRESO", "30.00")));
        verify(movimientoCajaRepository, never()).save(any(MovimientoCajaEntity.class));
    }

    private MovimientoCaja movimiento(String tipo, String monto) {
        return MovimientoCaja.builder()
                .fechaMovimiento(LocalDateTime.now())
                .tipo(tipo)
                .monto(new BigDecimal(monto))
                .descripcion("Movimiento")
                .cuentaCajaId(1L)
                .categoriaMovimientoId(2L)
                .build();
    }
}
