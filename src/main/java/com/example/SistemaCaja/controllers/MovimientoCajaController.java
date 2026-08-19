package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.MovimientoCaja;
import com.example.SistemaCaja.services.ICategoriaMovimientoService;
import com.example.SistemaCaja.services.ICuentaCajaService;
import com.example.SistemaCaja.services.IMovimientoCajaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/movimientos-caja")
@RequiredArgsConstructor
public class MovimientoCajaController {

    private final IMovimientoCajaService movimientoCajaService;
    private final ICuentaCajaService cuentaCajaService;
    private final ICategoriaMovimientoService categoriaMovimientoService;

    @GetMapping
    public String listarMovimientos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long cuentaCajaId,
            Model model) {
        model.addAttribute("movimientos", movimientoCajaService.obtenerTodos(fechaInicio, fechaFin, tipo, cuentaCajaId));
        model.addAttribute("cuentas", cuentaCajaService.obtenerTodas());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("cuentaSeleccionada", cuentaCajaId);
        return "movimientos-caja/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        MovimientoCaja movimientoCaja = new MovimientoCaja();
        movimientoCaja.setFechaMovimiento(LocalDateTime.now());
        prepararFormulario(model, movimientoCaja, "Registrar Movimiento");
        return "movimientos-caja/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        MovimientoCaja movimientoCaja = movimientoCajaService.obtenerPorId(id);
        if (movimientoCaja == null) {
            return "redirect:/movimientos-caja";
        }
        prepararFormulario(model, movimientoCaja, "Editar Movimiento");
        return "movimientos-caja/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMovimiento(
            @Valid @ModelAttribute("movimientoCaja") MovimientoCaja movimientoCaja,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            prepararFormulario(model, movimientoCaja,
                    movimientoCaja.getId() == null ? "Registrar Movimiento" : "Editar Movimiento");
            return "movimientos-caja/formulario";
        }
        movimientoCajaService.guardar(movimientoCaja);
        return "redirect:/movimientos-caja";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMovimiento(@PathVariable Long id) {
        movimientoCajaService.eliminar(id);
        return "redirect:/movimientos-caja";
    }

    private void prepararFormulario(Model model, MovimientoCaja movimientoCaja, String titulo) {
        model.addAttribute("movimientoCaja", movimientoCaja);
        model.addAttribute("cuentas", cuentaCajaService.obtenerTodas());
        model.addAttribute("categorias", categoriaMovimientoService.obtenerTodas(null));
        model.addAttribute("titulo", titulo);
    }
}
