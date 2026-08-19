package com.example.SistemaCaja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndicadorLiquidezResponse {
    private Long id;
    private Long empresaId;
    private String nombreEmpresa;
    private String periodo;
    private BigDecimal capitalTrabajo;
    private BigDecimal razonCorriente;
    private Integer diasCajaDisponible;
    private String nivelRiesgo;
    private LocalDateTime fechaCalculo;
}
