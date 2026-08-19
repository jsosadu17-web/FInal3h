package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.dto.DashboardConsolidadoResponse;
import com.example.SistemaCaja.dto.IndicadorLiquidezResponse;
import com.example.SistemaCaja.services.IIndicadorLiquidezService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicadores")
@RequiredArgsConstructor
public class IndicadorController {

    private final IIndicadorLiquidezService indicadorService;

    @PostMapping("/calcular/{empresaId}")
    public ResponseEntity<IndicadorLiquidezResponse> calcularIndicador(
            @PathVariable Long empresaId,
            @RequestParam String periodo) {
        return ResponseEntity.ok(indicadorService.calcularYGuardarIndicador(empresaId, periodo));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<IndicadorLiquidezResponse> obtenerPorPeriodo(
            @PathVariable Long empresaId,
            @RequestParam String periodo) {
        return ResponseEntity.ok(indicadorService.obtenerPorEmpresaYPeriodo(empresaId, periodo));
    }

    @GetMapping("/dashboard/{empresaId}")
    public ResponseEntity<DashboardConsolidadoResponse> obtenerDashboard(@PathVariable Long empresaId) {
        return ResponseEntity.ok(indicadorService.obtenerDashboardConsolidado(empresaId));
    }
}