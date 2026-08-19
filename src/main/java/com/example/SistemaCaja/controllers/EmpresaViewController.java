package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.Empresa;
import com.example.SistemaCaja.services.IEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaViewController {

    private final IEmpresaService empresaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", empresaService.obtenerTodas());
        return "empresas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empresa", new Empresa());
        model.addAttribute("titulo", "Registrar Empresa");
        return "empresas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Empresa empresa = empresaService.obtenerPorId(id);
        if (empresa == null) {
            return "redirect:/empresas";
        }
        model.addAttribute("empresa", empresa);
        model.addAttribute("titulo", "Editar Empresa");
        return "empresas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empresa") Empresa empresa,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", empresa.getId() == null ? "Registrar Empresa" : "Editar Empresa");
            return "empresas/formulario";
        }
        empresaService.guardar(empresa);
        return "redirect:/empresas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        empresaService.eliminar(id);
        return "redirect:/empresas";
    }
}