package com.example.SistemaCaja.services;

import com.example.SistemaCaja.entities.UsuarioEntity;
import java.util.Optional;

public interface IUsuarioService {
    UsuarioEntity registrar(UsuarioEntity usuario);
    Optional<UsuarioEntity> buscarPorUsername(String username);
    UsuarioEntity autenticar(String username, String password);
}