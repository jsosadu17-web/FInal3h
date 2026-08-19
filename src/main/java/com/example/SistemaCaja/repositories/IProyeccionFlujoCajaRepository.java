package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.ProyeccionFlujoCajaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProyeccionFlujoCajaRepository extends JpaRepository<ProyeccionFlujoCajaEntity, Long> {
    List<ProyeccionFlujoCajaEntity> findByEscenarioIdOrderByNumeroPeriodoAsc(Long escenarioId);
    List<ProyeccionFlujoCajaEntity> findByEmpresaIdOrderByFechaGeneracionDesc(Long empresaId);

    @Modifying
    @Query("delete from ProyeccionFlujoCajaEntity p where p.escenario.id = :escenarioId")
    void deleteByEscenarioId(@Param("escenarioId") Long escenarioId);
}
