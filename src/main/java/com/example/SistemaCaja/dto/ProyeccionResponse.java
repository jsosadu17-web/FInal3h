package com.example.SistemaCaja.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ProyeccionResponse {
    private Integer numeroPeriodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal ingresosBase;
    private BigDecimal egresosBase;
    private BigDecimal ingresosProyectados;
    private BigDecimal egresosProyectados;
    private BigDecimal flujoNetoProyectado;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinalProyectado;
}
