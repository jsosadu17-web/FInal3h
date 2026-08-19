package com.example.SistemaCaja.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_movimiento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaMovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;
}
