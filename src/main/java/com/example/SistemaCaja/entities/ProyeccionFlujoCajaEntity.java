package com.example.SistemaCaja.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyeccion_flujo_caja", indexes = {
        @Index(name = "idx_proyeccion_escenario_periodo", columnList = "escenario_id,numero_periodo"),
        @Index(name = "idx_proyeccion_empresa", columnList = "empresa_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyeccionFlujoCajaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "escenario_id", nullable = false)
    private EscenarioEntity escenario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

    @Column(nullable = false)
    private Integer numeroPeriodo;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal ingresosBase;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal egresosBase;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal ingresosProyectados;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal egresosProyectados;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal flujoNetoProyectado;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoFinalProyectado;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;
}
