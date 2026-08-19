package com.example.SistemaCaja.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aspectj.bridge.IMessage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaCaja {

    private Long id;

    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String numeroCuenta;

    @NotBlank(message = "El nombre de la Cuenta es obligatorio")
    private String nombreCuenta;

    @NotNull(message = "El saldo no puede ser nulo")
    private Double saldo;

    private Empresa empresa;
}
