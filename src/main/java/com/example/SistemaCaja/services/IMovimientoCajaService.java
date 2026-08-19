package com.example.SistemaCaja.services;

import com.example.SistemaCaja.models.MovimientoCaja;

import java.time.LocalDate;
import java.util.List;

public interface IMovimientoCajaService {
    List<MovimientoCaja> obtenerTodos(LocalDate fechaInicio, LocalDate fechaFin, String tipo, Long cuentaCajaId);
    MovimientoCaja obtenerPorId(Long id);
    MovimientoCaja guardar(MovimientoCaja movimientoCaja);
    void eliminar(Long id);
}
