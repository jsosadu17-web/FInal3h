package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.models.Empresa;
import com.example.SistemaCaja.services.IEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final IEmpresaService empresaService;

    // 1. Obtener todas las empresas: GET /api/empresas
    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.obtenerTodas());
    }

    // 2. Obtener empresa por ID: GET /api/empresas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerPorId(@PathVariable Long id) {
        Empresa empresa = empresaService.obtenerPorId(id);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empresa);
    }

    // 3. Crear empresa: POST /api/empresas
    @PostMapping
    public ResponseEntity<Empresa> guardarEmpresa(@Valid @RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.guardar(empresa));
    }

    // 4. Eliminar empresa: DELETE /api/empresas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}