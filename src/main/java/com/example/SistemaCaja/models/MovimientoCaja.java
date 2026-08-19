package com.example.SistemaCaja.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCaja {

    private Long id;

    @NotNull(message = "La fecha del movimiento es obligatoria")
    private LocalDateTime fechaMovimiento;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "INGRESO|EGRESO", message = "El tipo debe ser INGRESO o EGRESO")
    private String tipo;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    @NotBlank(message = "La descripción del movimiento es obligatoria")
    private String descripcion;

    @NotNull(message = "La cuenta de caja es obligatoria")
    private Long cuentaCajaId;

    @NotNull(message = "La categoría del movimiento es obligatoria")
    private Long categoriaMovimientoId;

    private String nombreCuenta;
    private String nombreCategoria;
}
