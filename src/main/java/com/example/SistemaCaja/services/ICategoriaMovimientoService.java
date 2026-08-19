package com.example.SistemaCaja.services;

import com.example.SistemaCaja.models.CategoriaMovimiento;

import java.util.List;

public interface ICategoriaMovimientoService {
    List<CategoriaMovimiento> obtenerTodas(String tipo);
    CategoriaMovimiento obtenerPorId(Long id);
    CategoriaMovimiento guardar(CategoriaMovimiento categoriaMovimiento);
    void eliminar(Long id);
}
