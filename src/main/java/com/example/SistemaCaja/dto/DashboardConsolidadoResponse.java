package com.example.SistemaCaja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardConsolidadoResponse {
    private Long empresaId;
    private String nombreEmpresa;
    private BigDecimal totalIngresosReales;
    private BigDecimal totalEgresosReales;
    private BigDecimal saldoCajaActual;
    private BigDecimal totalIngresosProyectados;
    private BigDecimal totalEgresosProyectados;
    private IndicadorLiquidezResponse ultimoIndicador;
    private List<IndicadorLiquidezResponse> historialIndicadores;
}