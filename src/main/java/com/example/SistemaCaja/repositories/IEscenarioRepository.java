package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.EscenarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEscenarioRepository extends JpaRepository<EscenarioEntity, Long> {
    List<EscenarioEntity> findByEmpresaId(Long empresaId);
    List<EscenarioEntity> findByEmpresaIdAndActivoTrue(Long empresaId);
    boolean existsByNombreIgnoreCaseAndEmpresaId(String nombre, Long empresaId);
    boolean existsByNombreIgnoreCaseAndEmpresaIdAndIdNot(String nombre, Long empresaId, Long id);
}
