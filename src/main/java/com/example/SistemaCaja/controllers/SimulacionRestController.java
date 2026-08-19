package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.dto.*;
import com.example.SistemaCaja.services.ISimulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulaciones")
@RequiredArgsConstructor
public class SimulacionRestController {
    private final ISimulacionService simulacionService;

    @PostMapping public ResponseEntity<SimulacionResponse> ejecutar(@Valid @RequestBody SimulacionRequest request) { return ResponseEntity.ok(simulacionService.ejecutar(request)); }
    @GetMapping("/escenario/{escenarioId}") public ResponseEntity<List<ProyeccionResponse>> porEscenario(@PathVariable Long escenarioId) { return ResponseEntity.ok(simulacionService.listarPorEscenario(escenarioId)); }
    @GetMapping("/empresa/{empresaId}") public ResponseEntity<List<ProyeccionResponse>> porEmpresa(@PathVariable Long empresaId) { return ResponseEntity.ok(simulacionService.listarPorEmpresa(empresaId)); }
    @DeleteMapping("/escenario/{escenarioId}") public ResponseEntity<Void> eliminar(@PathVariable Long escenarioId) { simulacionService.eliminarPorEscenario(escenarioId); return ResponseEntity.noContent().build(); }
}
