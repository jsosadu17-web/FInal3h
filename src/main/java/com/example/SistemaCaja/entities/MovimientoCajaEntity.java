package com.example.SistemaCaja.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_caja")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCajaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_caja_id", nullable = false)
    private CuentaCajaEntity cuentaCaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_movimiento_id", nullable = false)
    private CategoriaMovimientoEntity categoriaMovimiento;
}
