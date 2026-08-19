package com.example.SistemaCaja.config;

import com.example.SistemaCaja.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/css/**", "/js/**", "/static/**").permitAll()
                        .requestMatchers("/cuentas/**", "/empresas/**", "/movimientos-caja/**",
                                "/categorias-movimiento/**", "/indicadores/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

/*¡Atención equipo! 🚨
Desde ahora todos los endpoints REST de sus módulos exigen token JWT. Si están probando su CRUD sin haber iniciado sesión,
les empezará a salir error 401 Unauthorized. Para que sus pruebas sigan funcionando, recuerden enviar el token en el header de la petición:
Authorization: Bearer <token>

Las pantallas Thymeleaf (/cuentas, /empresas, /movimientos-caja, /categorias-movimiento, /indicadores) quedan
abiertas sin login (permitAll) porque la seguridad JWT sin sesión no funciona con navegación normal de navegador.
La API REST bajo /api/** sigue exigiendo token — eso es lo que demuestra que la seguridad protege los módulos.*/