package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.CategoriaMovimiento;
import com.example.SistemaCaja.services.ICategoriaMovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias-movimiento")
@RequiredArgsConstructor
public class CategoriaMovimientoController {

    private final ICategoriaMovimientoService categoriaMovimientoService;

    @GetMapping
    public String listar(@RequestParam(required = false) String tipo, Model model) {
        String tipoNormalizado = tipo == null || tipo.isBlank() ? null : tipo;
        model.addAttribute("categorias",
                categoriaMovimientoService.obtenerTodas(tipoNormalizado));
        model.addAttribute("tipoSeleccionado", tipoNormalizado);
        return "categorias-movimiento/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoriaMovimiento", new CategoriaMovimiento());
        model.addAttribute("titulo", "Nueva Categoría");
        return "categorias-movimiento/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("categoriaMovimiento") CategoriaMovimiento categoriaMovimiento,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo",
                    categoriaMovimiento.getId() == null
                            ? "Nueva Categoría"
                            : "Editar Categoría");
            return "categorias-movimiento/formulario";
        }

        categoriaMovimientoService.guardar(categoriaMovimiento);
        return "redirect:/categorias-movimiento";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        CategoriaMovimiento categoria =
                categoriaMovimientoService.obtenerPorId(id);

        if (categoria == null) {
            return "redirect:/categorias-movimiento";
        }

        model.addAttribute("categoriaMovimiento", categoria);
        model.addAttribute("titulo", "Editar Categoría");

        return "categorias-movimiento/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        categoriaMovimientoService.eliminar(id);

        return "redirect:/categorias-movimiento";
    }
}