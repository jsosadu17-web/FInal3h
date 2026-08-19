package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.IndicadorLiquidezEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIndicadorLiquidezRepository extends JpaRepository<IndicadorLiquidezEntity, Long> {

    Optional<IndicadorLiquidezEntity> findByEmpresaIdAndPeriodo(Long empresaId, String periodo);

    List<IndicadorLiquidezEntity> findByEmpresaId(Long empresaId);
}