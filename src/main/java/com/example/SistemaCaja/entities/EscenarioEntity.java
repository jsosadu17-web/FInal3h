package com.example.SistemaCaja.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "escenario", uniqueConstraints = @UniqueConstraint(name = "uk_escenario_empresa_nombre", columnNames = {"empresa_id", "nombre"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscenarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal variacionIngresosPorcentaje;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal variacionEgresosPorcentaje;

    @Column(nullable = false)
    private Integer numeroPeriodos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPeriodo tipoPeriodo;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        fechaCreacion = ahora;
        fechaActualizacion = ahora;
        if (activo == null) activo = true;
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
