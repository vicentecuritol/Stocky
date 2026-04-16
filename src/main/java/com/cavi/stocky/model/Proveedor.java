package com.cavi.stocky.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "proveedor")

public class Proveedor {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
}
