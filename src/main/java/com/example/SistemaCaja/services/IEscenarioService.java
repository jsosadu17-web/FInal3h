package com.example.SistemaCaja.services;

import com.example.SistemaCaja.dto.EscenarioRequest;
import com.example.SistemaCaja.dto.EscenarioResponse;

import java.util.List;

public interface IEscenarioService {
    EscenarioResponse crear(EscenarioRequest request);
    List<EscenarioResponse> listar();
    EscenarioResponse obtenerPorId(Long id);
    List<EscenarioResponse> listarPorEmpresa(Long empresaId);
    EscenarioResponse actualizar(Long id, EscenarioRequest request);
    void eliminar(Long id);
}
