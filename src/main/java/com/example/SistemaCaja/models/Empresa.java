package com.example.SistemaCaja.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {
    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11,max=11,message = "El RUC debe tener 11 digitos")
    private String ruc;

    @NotNull(message = "La Razon Social es obligatoria")
    private String razonSocial;

    private String direccion;
    private String telefono;

}
