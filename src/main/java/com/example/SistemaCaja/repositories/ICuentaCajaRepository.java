package com.example.SistemaCaja.repositories;

import com.example.SistemaCaja.entities.CuentaCajaEntity;
import com.example.SistemaCaja.models.CuentaCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICuentaCajaRepository extends JpaRepository<CuentaCajaEntity,Long> {
    //Jpa ya tiene incluido los metodos CRUD save(), findAll(), findById() y deleteById() automáticamente para las cuentas.

    // Método personalizado con SQL Nativo
    @Query(value = "Select * FROM cuenta_caja c WHERE c.empresa_id = ?1",nativeQuery =true)
    List<CuentaCajaEntity> buscarPorEmpresaIdNative(Long empresaId);

    @Query("SELECT COALESCE(SUM(c.saldo), 0) FROM CuentaCajaEntity c WHERE c.empresa.id = :empresaId")
    Double sumarSaldoPorEmpresa(@org.springframework.data.repository.query.Param("empresaId") Long empresaId);


}
