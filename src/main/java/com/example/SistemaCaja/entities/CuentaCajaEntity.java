package com.example.SistemaCaja.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "cuenta_caja")
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCajaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCuenta;
    private String nombreCuenta;
    private Double saldo;

    // Relación Muchosa 1 con EmpresaEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id") // Nombre de la columna FK en MySQL
    private EmpresaEntity empresa;
}
