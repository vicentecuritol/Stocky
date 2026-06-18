package com.cavi.stocky.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario. Valores esperados: "ROLE_USER" o "ROLE_ADMIN".
     * Spring Security usa el prefijo ROLE_ para los métodos hasRole().
     */
    @Column(nullable = false)
    private String role;
}
