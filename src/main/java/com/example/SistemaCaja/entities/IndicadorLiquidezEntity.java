package com.example.SistemaCaja.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "indicadores_liquidez")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicadorLiquidezEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal capitalTrabajo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal razonCorriente;

    @Column(nullable = false)
    private Integer diasCajaDisponible;

    @Column(nullable = false, length = 10)
    private String nivelRiesgo;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;
}