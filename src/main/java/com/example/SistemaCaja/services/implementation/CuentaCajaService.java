package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.CuentaCajaEntity;
import com.example.SistemaCaja.entities.EmpresaEntity;
import com.example.SistemaCaja.models.CuentaCaja;
import com.example.SistemaCaja.models.Empresa;
import com.example.SistemaCaja.repositories.ICuentaCajaRepository;
import com.example.SistemaCaja.services.ICuentaCajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class CuentaCajaService implements ICuentaCajaService {

    private final ICuentaCajaRepository cuentaCajaRepository;


    @Override
    public List<CuentaCaja> obtenerTodas() {
        return cuentaCajaRepository.findAll()
                .stream()
                .map(this::convertirAModel)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaCaja obtenerPorId(Long id) {
        return cuentaCajaRepository.findById(id)
                .map(this::convertirAModel)
                .orElse(null);
    }

    @Override
    public List<CuentaCaja> obtenerPorEmpresaId(Long empresaId) {
        // Ejecuta el método nativo que creaste en ICuentaCajaRepository
        return cuentaCajaRepository.buscarPorEmpresaIdNative(empresaId)
                .stream()
                .map(this::convertirAModel)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaCaja guardar(CuentaCaja cuentaCaja) {
        CuentaCajaEntity entity= convertirAEntity(cuentaCaja);
        CuentaCajaEntity guardado = cuentaCajaRepository.save(entity);
        return convertirAModel(guardado);
    }

    @Override
    public void eliminar(Long id) {
        cuentaCajaRepository.deleteById(id);

    }

    // --- Métodos de Conversión (Entity <-> Model) ---

    private CuentaCaja convertirAModel(CuentaCajaEntity entity){
        if(entity ==null) return null;
        return CuentaCaja.builder()
                .id(entity.getId())
                .numeroCuenta(entity.getNumeroCuenta())
                .nombreCuenta(entity.getNombreCuenta())
                .saldo(entity.getSaldo())
                .empresa(entity.getEmpresa() != null ? Empresa.builder()
                        .id(entity.getEmpresa().getId())
                        .ruc(entity.getEmpresa().getRuc())
                        .razonSocial(entity.getEmpresa().getRazonSocial())
                        .direccion(entity.getEmpresa().getDireccion())
                        .telefono(entity.getEmpresa().getTelefono())
                        .build() : null)
                .build();
    }

    private CuentaCajaEntity convertirAEntity(CuentaCaja model){
        if(model == null) return null;
        return CuentaCajaEntity.builder()
                .id(model.getId())
                .numeroCuenta(model.getNumeroCuenta())
                .nombreCuenta(model.getNombreCuenta())
                .saldo(model.getSaldo())
                .empresa(model.getEmpresa() != null ? EmpresaEntity.builder()
                        .id(model.getEmpresa().getId())
                        .build() : null)
                .build();
    }

}
