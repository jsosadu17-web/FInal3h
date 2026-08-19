package com.example.SistemaCaja.services;

import com.example.SistemaCaja.models.CuentaCaja;

import java.util.List;

public interface ICuentaCajaService {

    //Listar todas las cuentas de caja/bancarias
    List<CuentaCaja> obtenerTodas();

    //Buscar una cuenta por su ID
    CuentaCaja obtenerPorId(Long id);

    //Listar solo las cuentas pertenecientes a una empresa
    List<CuentaCaja> obtenerPorEmpresaId(Long empresaId);

    //Registrar o actualizar una cuenta
    CuentaCaja guardar(CuentaCaja cuentaCaja);

    //Eliminar una cuenta por ID
    void eliminar(Long id);
}
