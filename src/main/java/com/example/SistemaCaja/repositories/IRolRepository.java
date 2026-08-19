package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolRepository extends JpaRepository<RolEntity, Long> {
}