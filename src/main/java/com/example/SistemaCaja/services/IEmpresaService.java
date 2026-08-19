package com.example.SistemaCaja.services;

import com.example.SistemaCaja.models.Empresa;

import java.util.List;

public interface IEmpresaService {
    //Listar todas las empresas
    List<Empresa> obtenerTodas();

    //Buscar una empresa por ID
    Empresa obtenerPorId(Long id);

    //Guardar una nueva empresa o actualizar una existente
    Empresa guardar(Empresa empresa);

    //Eliminar una empresa por ID
    void eliminar(Long id);
}
