package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.MovimientoCajaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMovimientoCajaRepository extends JpaRepository<MovimientoCajaEntity, Long> {

    @Query("""
            SELECT m
            FROM MovimientoCajaEntity m
            JOIN FETCH m.cuentaCaja c
            JOIN FETCH m.categoriaMovimiento cm
            WHERE (:fechaInicio IS NULL OR m.fechaMovimiento >= :fechaInicio)
              AND (:fechaFin IS NULL OR m.fechaMovimiento <= :fechaFin)
              AND (:tipo IS NULL OR m.tipo = :tipo)
              AND (:cuentaCajaId IS NULL OR c.id = :cuentaCajaId)
            ORDER BY m.fechaMovimiento DESC
            """)
    List<MovimientoCajaEntity> buscarFiltrado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("tipo") String tipo,
            @Param("cuentaCajaId") Long cuentaCajaId);

    @Query("""
            SELECT m
            FROM MovimientoCajaEntity m
            JOIN FETCH m.cuentaCaja c
            JOIN FETCH c.empresa e
            WHERE e.id = :empresaId
              AND m.fechaMovimiento >= :fechaInicio
              AND m.fechaMovimiento <= :fechaFin
            ORDER BY m.fechaMovimiento ASC
            """)
    List<MovimientoCajaEntity> buscarPorEmpresaYPeriodo(
            @Param("empresaId") Long empresaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
