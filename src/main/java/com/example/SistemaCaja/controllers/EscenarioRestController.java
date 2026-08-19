package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.dto.*;
import com.example.SistemaCaja.services.IEscenarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escenarios")
@RequiredArgsConstructor
public class EscenarioRestController {
    private final IEscenarioService escenarioService;

    @GetMapping public ResponseEntity<List<EscenarioResponse>> listar() { return ResponseEntity.ok(escenarioService.listar()); }
    @GetMapping("/{id}") public ResponseEntity<EscenarioResponse> obtener(@PathVariable Long id) { return ResponseEntity.ok(escenarioService.obtenerPorId(id)); }
    @GetMapping("/empresa/{empresaId}") public ResponseEntity<List<EscenarioResponse>> porEmpresa(@PathVariable Long empresaId) { return ResponseEntity.ok(escenarioService.listarPorEmpresa(empresaId)); }
    @PostMapping public ResponseEntity<EscenarioResponse> crear(@Valid @RequestBody EscenarioRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(escenarioService.crear(request)); }
    @PutMapping("/{id}") public ResponseEntity<EscenarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody EscenarioRequest request) { return ResponseEntity.ok(escenarioService.actualizar(id, request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id) { escenarioService.eliminar(id); return ResponseEntity.noContent().build(); }
}
