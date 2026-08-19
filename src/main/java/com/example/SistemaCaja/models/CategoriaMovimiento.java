package com.example.SistemaCaja.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaMovimiento {

    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "INGRESO|EGRESO", message = "El tipo debe ser INGRESO o EGRESO")
    private String tipo;

    private String descripcion;

    private Boolean activo;
}
