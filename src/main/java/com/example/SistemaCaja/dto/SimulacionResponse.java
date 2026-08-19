package com.example.SistemaCaja.dto;

import com.example.SistemaCaja.entities.TipoPeriodo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SimulacionResponse {
    private Long escenarioId;
    private Long empresaId;
    private String nombreEscenario;
    private TipoPeriodo tipoPeriodo;
    private LocalDate fechaInicioHistorial;
    private LocalDate fechaFinHistorial;
    private Integer cantidadPeriodosHistoricos;
    private BigDecimal saldoInicialTotal;
    private Integer totalPeriodosProyectados;
    private LocalDateTime fechaGeneracion;
    private List<ProyeccionResponse> proyecciones;
}
