package com.example.SistemaCaja.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class SimulacionRequest {
    @NotNull(message = "El escenario es obligatorio")
    private Long escenarioId;
    @NotNull(message = "La fecha inicial es obligatoria")
    private LocalDate fechaInicioHistorial;
    @NotNull(message = "La fecha final es obligatoria")
    private LocalDate fechaFinHistorial;
    @Builder.Default
    private Boolean guardarResultado = true;

    @AssertTrue(message = "La fecha inicial no puede ser posterior a la fecha final")
    public boolean isRangoValido() {
        return fechaInicioHistorial == null || fechaFinHistorial == null || !fechaInicioHistorial.isAfter(fechaFinHistorial);
    }
}
