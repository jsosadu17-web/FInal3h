package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.dto.DashboardConsolidadoResponse;
import com.example.SistemaCaja.dto.IndicadorLiquidezResponse;
import com.example.SistemaCaja.services.IEmpresaService;
import com.example.SistemaCaja.services.IIndicadorLiquidezService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/indicadores")
@RequiredArgsConstructor
public class IndicadorViewController {

    private final IIndicadorLiquidezService indicadorService;
    private final IEmpresaService empresaService;

    @GetMapping("/dashboard/{empresaId}")
    public String verDashboard(@PathVariable Long empresaId, Model model) {
        DashboardConsolidadoResponse dashboard = indicadorService.obtenerDashboardConsolidado(empresaId);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("empresas", empresaService.obtenerTodas());
        model.addAttribute("empresaSeleccionadaId", empresaId);
        model.addAttribute("titulo", "Dashboard de Liquidez y Riesgo");
        return "indicadores/dashboard";
    }

    @GetMapping("/calcular")
    public String mostrarFormularioCalculo(Model model) {
        model.addAttribute("empresas", empresaService.obtenerTodas());
        model.addAttribute("titulo", "Calcular Indicadores de Liquidez");
        return "indicadores/calcular";
    }

    @PostMapping("/calcular")
    public String procesarCalculo(@RequestParam Long empresaId,
                                  @RequestParam String periodo,
                                  Model model) {

        if (periodo != null && periodo.length() > 7) {
            periodo = periodo.substring(0, 7);
        }

        IndicadorLiquidezResponse resultado = indicadorService.calcularYGuardarIndicador(empresaId, periodo);
        model.addAttribute("resultado", resultado);
        model.addAttribute("empresas", empresaService.obtenerTodas());
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("periodo", periodo);
        model.addAttribute("titulo", "Calcular Indicadores de Liquidez");
        return "indicadores/calcular";
    }
}