package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpresaRepository extends JpaRepository<EmpresaEntity,Long> {
    //Queda vacio JpaRepository ya tiene todos los metodos CRUD
    //save(...)  Guarda una nueva empresa o actualiza una existente.
    //findAll() ➡️ Trae la lista de todas las empresas.
    //findById(id) ➡️ Busca una empresa por su ID.
    //deleteById(id) ➡️ Elimina una empresa por su ID.
}
