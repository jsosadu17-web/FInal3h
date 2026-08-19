package com.example.SistemaCaja.dto;

import com.example.SistemaCaja.entities.TipoPeriodo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class EscenarioResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long empresaId;
    private BigDecimal variacionIngresosPorcentaje;
    private BigDecimal variacionEgresosPorcentaje;
    private Integer numeroPeriodos;
    private TipoPeriodo tipoPeriodo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
