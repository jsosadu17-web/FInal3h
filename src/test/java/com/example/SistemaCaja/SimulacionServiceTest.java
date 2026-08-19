package com.example.SistemaCaja;

import com.example.SistemaCaja.dto.*;
import com.example.SistemaCaja.entities.*;
import com.example.SistemaCaja.repositories.*;
import com.example.SistemaCaja.services.implementation.SimulacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulacionServiceTest {
    @Mock IEscenarioRepository escenarioRepository;
    @Mock IEmpresaRepository empresaRepository;
    @Mock ICuentaCajaRepository cuentaCajaRepository;
    @Mock IMovimientoCajaRepository movimientoRepository;
    @Mock IProyeccionFlujoCajaRepository proyeccionRepository;
    @InjectMocks SimulacionService service;

    @Test
    void proyectaMensualYArrastraSaldoAlSiguientePeriodo() {
        EmpresaEntity empresa = EmpresaEntity.builder().id(1L).build();
        EscenarioEntity escenario = escenario(empresa, TipoPeriodo.MENSUAL, BigDecimal.ZERO, BigDecimal.ZERO, 2);
        when(escenarioRepository.findById(1L)).thenReturn(Optional.of(escenario));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(cuentaCajaRepository.buscarPorEmpresaIdNative(1L)).thenReturn(List.of(CuentaCajaEntity.builder().id(2L).build()));
        when(cuentaCajaRepository.sumarSaldoPorEmpresa(1L)).thenReturn(1000D);
        when(movimientoRepository.buscarPorEmpresaYPeriodo(eq(1L), any(), any())).thenReturn(List.of(
                MovimientoCajaEntity.builder().tipo("INGRESO").monto(new BigDecimal("300.00")).build(),
                MovimientoCajaEntity.builder().tipo("EGRESO").monto(new BigDecimal("100.00")).build()));

        SimulacionResponse response = service.ejecutar(SimulacionRequest.builder().escenarioId(1L)
                .fechaInicioHistorial(LocalDate.of(2026, 1, 1)).fechaFinHistorial(LocalDate.of(2026, 2, 28)).guardarResultado(false).build());

        assertEquals(new BigDecimal("150.00"), response.getProyecciones().get(0).getIngresosProyectados());
        assertEquals(new BigDecimal("1100.00"), response.getProyecciones().get(0).getSaldoFinalProyectado());
        assertEquals(new BigDecimal("1100.00"), response.getProyecciones().get(1).getSaldoInicial());
        verify(proyeccionRepository, never()).saveAll(any());
    }

    @Test
    void reemplazaProyeccionesCuandoSeSolicitaGuardar() {
        EmpresaEntity empresa = EmpresaEntity.builder().id(1L).build();
        when(escenarioRepository.findById(1L)).thenReturn(Optional.of(escenario(empresa, TipoPeriodo.SEMANAL, new BigDecimal("10"), new BigDecimal("-10"), 1)));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(cuentaCajaRepository.buscarPorEmpresaIdNative(1L)).thenReturn(List.of(CuentaCajaEntity.builder().id(2L).build()));
        when(cuentaCajaRepository.sumarSaldoPorEmpresa(1L)).thenReturn(0D);
        when(movimientoRepository.buscarPorEmpresaYPeriodo(anyLong(), any(), any())).thenReturn(List.of(
                MovimientoCajaEntity.builder().tipo("INGRESO").monto(new BigDecimal("100.00")).build(),
                MovimientoCajaEntity.builder().tipo("EGRESO").monto(new BigDecimal("50.00")).build()));

        service.ejecutar(SimulacionRequest.builder().escenarioId(1L).fechaInicioHistorial(LocalDate.of(2026, 1, 1))
                .fechaFinHistorial(LocalDate.of(2026, 1, 7)).guardarResultado(true).build());

        verify(proyeccionRepository).deleteByEscenarioId(1L);
        verify(proyeccionRepository).saveAll(anyList());
    }

    @Test
    void rechazaEscenarioInexistente() {
        when(escenarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.ejecutar(SimulacionRequest.builder().escenarioId(99L)
                .fechaInicioHistorial(LocalDate.now()).fechaFinHistorial(LocalDate.now()).build()));
    }

    private EscenarioEntity escenario(EmpresaEntity empresa, TipoPeriodo tipo, BigDecimal varIngresos, BigDecimal varEgresos, int periodos) {
        return EscenarioEntity.builder().id(1L).nombre("Base").empresa(empresa).activo(true)
                .tipoPeriodo(tipo).variacionIngresosPorcentaje(varIngresos).variacionEgresosPorcentaje(varEgresos).numeroPeriodos(periodos).build();
    }
}
