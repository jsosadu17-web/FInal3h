package com.example.SistemaCaja.services;

import com.example.SistemaCaja.dto.*;

import java.util.List;

public interface ISimulacionService {
    SimulacionResponse ejecutar(SimulacionRequest request);
    List<ProyeccionResponse> listarPorEscenario(Long escenarioId);
    List<ProyeccionResponse> listarPorEmpresa(Long empresaId);
    void eliminarPorEscenario(Long escenarioId);
}
