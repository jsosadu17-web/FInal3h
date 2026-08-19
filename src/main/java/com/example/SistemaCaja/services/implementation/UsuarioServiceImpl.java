package com.example.SistemaCaja.services.implementation;

import com.example.SistemaCaja.entities.UsuarioEntity;
import com.example.SistemaCaja.exceptions.CredencialesInvalidasException;
import com.example.SistemaCaja.repositories.IUsuarioRepository;
import com.example.SistemaCaja.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioEntity registrar(UsuarioEntity usuario) {
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public UsuarioEntity autenticar(String username, String password) {
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }
        return usuario;
    }
}