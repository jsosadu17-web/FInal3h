package com.example.SistemaCaja.services;

import com.example.SistemaCaja.dto.DashboardConsolidadoResponse;
import com.example.SistemaCaja.dto.IndicadorLiquidezResponse;

import java.util.List;

public interface IIndicadorLiquidezService {
    IndicadorLiquidezResponse calcularYGuardarIndicador(Long empresaId, String periodo);
    IndicadorLiquidezResponse obtenerPorEmpresaYPeriodo(Long empresaId, String periodo);
    DashboardConsolidadoResponse obtenerDashboardConsolidado(Long empresaId);
}
