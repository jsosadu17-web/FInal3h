package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.CategoriaMovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoriaMovimientoRepository extends JpaRepository<CategoriaMovimientoEntity, Long> {
    List<CategoriaMovimientoEntity> findByTipo(String tipo);
}
