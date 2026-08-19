package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.dto.*;
import com.example.SistemaCaja.entities.*;
import com.example.SistemaCaja.exceptions.*;
import com.example.SistemaCaja.repositories.*;
import com.example.SistemaCaja.services.IEscenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EscenarioService implements IEscenarioService {
    private final IEscenarioRepository escenarioRepository;
    private final IEmpresaRepository empresaRepository;

    @Override @Transactional
    public EscenarioResponse crear(EscenarioRequest request) {
        EmpresaEntity empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La empresa no existe: " + request.getEmpresaId()));
        if (escenarioRepository.existsByNombreIgnoreCaseAndEmpresaId(request.getNombre(), request.getEmpresaId())) {
            throw new ReglaNegocioException("Ya existe un escenario con ese nombre para la empresa");
        }
        return toResponse(escenarioRepository.save(toEntity(request, empresa, null)));
    }

    @Override @Transactional(readOnly = true)
    public List<EscenarioResponse> listar() { return escenarioRepository.findAll().stream().map(this::toResponse).toList(); }

    @Override @Transactional(readOnly = true)
    public EscenarioResponse obtenerPorId(Long id) { return toResponse(find(id)); }

    @Override @Transactional(readOnly = true)
    public List<EscenarioResponse> listarPorEmpresa(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) throw new RecursoNoEncontradoException("La empresa no existe: " + empresaId);
        return escenarioRepository.findByEmpresaId(empresaId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public EscenarioResponse actualizar(Long id, EscenarioRequest request) {
        EscenarioEntity actual = find(id);
        EmpresaEntity empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La empresa no existe: " + request.getEmpresaId()));
        if (escenarioRepository.existsByNombreIgnoreCaseAndEmpresaIdAndIdNot(request.getNombre(), request.getEmpresaId(), id)) {
            throw new ReglaNegocioException("Ya existe un escenario con ese nombre para la empresa");
        }
        EscenarioEntity actualizado = toEntity(request, empresa, id);
        actualizado.setActivo(actual.getActivo());
        actualizado.setFechaCreacion(actual.getFechaCreacion());
        actualizado.setFechaActualizacion(actual.getFechaActualizacion());
        return toResponse(escenarioRepository.save(actualizado));
    }

    @Override @Transactional
    public void eliminar(Long id) {
        EscenarioEntity escenario = find(id);
        escenario.setActivo(false);
        escenarioRepository.save(escenario);
    }

    private EscenarioEntity find(Long id) {
        return escenarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("El escenario no existe: " + id));
    }

    private EscenarioEntity toEntity(EscenarioRequest r, EmpresaEntity empresa, Long id) {
        return EscenarioEntity.builder().id(id).nombre(r.getNombre().trim()).descripcion(r.getDescripcion()).empresa(empresa)
                .variacionIngresosPorcentaje(r.getVariacionIngresosPorcentaje())
                .variacionEgresosPorcentaje(r.getVariacionEgresosPorcentaje()).numeroPeriodos(r.getNumeroPeriodos())
                .tipoPeriodo(r.getTipoPeriodo()).activo(true).build();
    }

    private EscenarioResponse toResponse(EscenarioEntity e) {
        return EscenarioResponse.builder().id(e.getId()).nombre(e.getNombre()).descripcion(e.getDescripcion())
                .empresaId(e.getEmpresa().getId()).variacionIngresosPorcentaje(e.getVariacionIngresosPorcentaje())
                .variacionEgresosPorcentaje(e.getVariacionEgresosPorcentaje()).numeroPeriodos(e.getNumeroPeriodos())
                .tipoPeriodo(e.getTipoPeriodo()).activo(e.getActivo()).fechaCreacion(e.getFechaCreacion())
                .fechaActualizacion(e.getFechaActualizacion()).build();
    }
}
