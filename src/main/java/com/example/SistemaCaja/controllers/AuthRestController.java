package com.example.SistemaCaja.controllers;

import com.example.SistemaCaja.dto.LoginRequest;
import com.example.SistemaCaja.dto.LoginResponse;
import com.example.SistemaCaja.dto.RegistroRequest;
import com.example.SistemaCaja.entities.RolEntity;
import com.example.SistemaCaja.entities.UsuarioEntity;
import com.example.SistemaCaja.security.JwtUtil;
import com.example.SistemaCaja.services.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IUsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioEntity> registrar(@Valid @RequestBody RegistroRequest request) {
        UsuarioEntity nuevoUsuario = new UsuarioEntity();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPasswordHash(request.getPassword());

        RolEntity rol = new RolEntity();
        rol.setIdRol(request.getIdRol());
        nuevoUsuario.setRol(rol);

        UsuarioEntity guardado = usuarioService.registrar(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsuarioEntity usuario = usuarioService.autenticar(request.getUsername(), request.getPassword());
        String token = jwtUtil.generarToken(usuario.getUsername());

        LoginResponse response = new LoginResponse();
        response.setUsername(usuario.getUsername());
        response.setRol(usuario.getRol().getNombreRol());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}