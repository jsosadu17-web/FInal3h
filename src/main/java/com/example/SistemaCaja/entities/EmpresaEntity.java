package com.example.SistemaCaja.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@Entity
@Table(name = "empresa")
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;

    // Relación 1 a Muchos con CuentaCajaEntity
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<CuentaCajaEntity> cuentas;
}
