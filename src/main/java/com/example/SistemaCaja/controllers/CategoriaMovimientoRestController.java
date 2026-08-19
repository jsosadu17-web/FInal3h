package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.CategoriaMovimiento;
import com.example.SistemaCaja.services.ICategoriaMovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-movimiento")
@RequiredArgsConstructor
public class CategoriaMovimientoRestController {

    private final ICategoriaMovimientoService categoriaMovimientoService;

    @GetMapping
    public List<CategoriaMovimiento> listar(@RequestParam(required = false) String tipo) {
        return categoriaMovimientoService.obtenerTodas(tipo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaMovimiento> obtenerPorId(@PathVariable Long id) {
        CategoriaMovimiento categoriaMovimiento = categoriaMovimientoService.obtenerPorId(id);
        return categoriaMovimiento == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(categoriaMovimiento);
    }

    @PostMapping
    public ResponseEntity<CategoriaMovimiento> crear(@Valid @RequestBody CategoriaMovimiento categoriaMovimiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaMovimientoService.guardar(categoriaMovimiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaMovimiento> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaMovimiento categoriaMovimiento) {
        if (categoriaMovimientoService.obtenerPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaMovimiento.setId(id);
        return ResponseEntity.ok(categoriaMovimientoService.guardar(categoriaMovimiento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (categoriaMovimientoService.obtenerPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaMovimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
