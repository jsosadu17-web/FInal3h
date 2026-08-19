package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.CuentaCaja;
import com.example.SistemaCaja.services.ICuentaCajaService;
import com.example.SistemaCaja.services.IEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaCajaController {

    private final ICuentaCajaService cuentaCajaService;
    private final IEmpresaService empresaService;

    @GetMapping
    public String listarCuentas(Model model) {
        model.addAttribute("cuentas", cuentaCajaService.obtenerTodas());
        return "cuentas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCuenta(Model model) {
        model.addAttribute("cuentaCaja", new CuentaCaja());
        model.addAttribute("empresas", empresaService.obtenerTodas());
        model.addAttribute("titulo", "Registrar Cuenta");
        return "cuentas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCuenta(@Valid @ModelAttribute("cuentaCaja") CuentaCaja cuenta,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empresas", empresaService.obtenerTodas());
            model.addAttribute("titulo", cuenta.getId() == null ? "Registrar Cuenta" : "Editar Cuenta");
            return "cuentas/formulario";
        }
        cuentaCajaService.guardar(cuenta);
        return "redirect:/cuentas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("cuentaCaja", cuentaCajaService.obtenerPorId(id));
        model.addAttribute("empresas", empresaService.obtenerTodas());
        model.addAttribute("titulo", "Editar Cuenta");
        return "cuentas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCuenta(@PathVariable Long id) {
        cuentaCajaService.eliminar(id);
        return "redirect:/cuentas";
    }
}