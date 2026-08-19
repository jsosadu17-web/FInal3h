package com.example.SistemaCaja.dto;

import com.example.SistemaCaja.entities.TipoPeriodo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscenarioRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String descripcion;
    @NotNull(message = "La empresa es obligatoria")
    private Long empresaId;
    @NotNull @DecimalMin(value = "-100.00", message = "La variación de ingresos no puede ser menor a -100")
    private BigDecimal variacionIngresosPorcentaje;
    @NotNull @DecimalMin(value = "-100.00", message = "La variación de egresos no puede ser menor a -100")
    private BigDecimal variacionEgresosPorcentaje;
    @NotNull @Min(value = 1, message = "Debe existir al menos un periodo") @Max(value = 60, message = "No puede superar 60 periodos")
    private Integer numeroPeriodos;
    @NotNull(message = "El tipo de periodo es obligatorio")
    private TipoPeriodo tipoPeriodo;
}
