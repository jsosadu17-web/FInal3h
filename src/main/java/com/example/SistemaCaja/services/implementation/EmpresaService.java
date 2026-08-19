package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.EmpresaEntity;
import com.example.SistemaCaja.models.Empresa;
import com.example.SistemaCaja.services.IEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SistemaCaja.repositories.IEmpresaRepository;
@Service
@RequiredArgsConstructor
public class EmpresaService implements IEmpresaService {

    private final IEmpresaRepository empresaRepository;
    @Override
    public List<Empresa> obtenerTodas() {
        return empresaRepository.findAll()
                .stream()
                .map(this::convertirAModel)
                .collect(Collectors.toList());
    }

    @Override
    public Empresa obtenerPorId(Long id) {
        return empresaRepository.findById(id)
                .map(this::convertirAModel)
                .orElse(null);
    }

    @Override
    public Empresa guardar(Empresa empresa) {
        EmpresaEntity entity = convertirAEntity(empresa);
        EmpresaEntity guardado = empresaRepository.save(entity);
        return convertirAModel(guardado);
    }

    @Override
    public void eliminar(Long id) {
        empresaRepository.deleteById(id);
    }

    // --- Métodos de Conversión (Entity <-> Model) ---

    private Empresa convertirAModel(EmpresaEntity entity){
        if(entity ==null) return null;
        return Empresa.builder()
                .id(entity.getId())
                .ruc(entity.getRuc())
                .razonSocial(entity.getRazonSocial())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .build();
    }

    private EmpresaEntity convertirAEntity(Empresa model){
        if(model == null) return null;
        return EmpresaEntity.builder()
                .id(model.getId())
                .ruc(model.getRuc())
                .razonSocial(model.getRazonSocial())
                .direccion(model.getDireccion())
                .telefono(model.getTelefono())
                .build();
    }
}
