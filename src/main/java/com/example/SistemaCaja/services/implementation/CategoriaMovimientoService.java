package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.CategoriaMovimientoEntity;
import com.example.SistemaCaja.models.CategoriaMovimiento;
import com.example.SistemaCaja.repositories.ICategoriaMovimientoRepository;
import com.example.SistemaCaja.services.ICategoriaMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaMovimientoService implements ICategoriaMovimientoService {

    private final ICategoriaMovimientoRepository categoriaMovimientoRepository;

    @Override
    public List<CategoriaMovimiento> obtenerTodas(String tipo) {
        List<CategoriaMovimientoEntity> categorias = tipo == null || tipo.isBlank()
                ? categoriaMovimientoRepository.findAll()
                : categoriaMovimientoRepository.findByTipo(tipo);

        return categorias.stream()
                .map(this::convertirAModel)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaMovimiento obtenerPorId(Long id) {
        return categoriaMovimientoRepository.findById(id)
                .map(this::convertirAModel)
                .orElse(null);
    }

    @Override
    public CategoriaMovimiento guardar(CategoriaMovimiento categoriaMovimiento) {
        CategoriaMovimientoEntity entity = convertirAEntity(categoriaMovimiento);
        if (entity.getActivo() == null) {
            entity.setActivo(true);
        }
        return convertirAModel(categoriaMovimientoRepository.save(entity));
    }

    @Override
    public void eliminar(Long id) {
        categoriaMovimientoRepository.deleteById(id);
    }

    private CategoriaMovimiento convertirAModel(CategoriaMovimientoEntity entity) {
        if (entity == null) return null;
        return CategoriaMovimiento.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .tipo(entity.getTipo())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .build();
    }

    private CategoriaMovimientoEntity convertirAEntity(CategoriaMovimiento model) {
        if (model == null) return null;
        return CategoriaMovimientoEntity.builder()
                .id(model.getId())
                .nombre(model.getNombre())
                .tipo(model.getTipo())
                .descripcion(model.getDescripcion())
                .activo(model.getActivo())
                .build();
    }
}
