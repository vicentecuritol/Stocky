package com.cavi.stocky.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "producto")

public class Producto {
    private Long id;
    private String nombre;
    private Integer precio;
    private Integer stockActual;
    private Integer stockMinimo;
    
    // Relaciones simples como objetos
    private Categoria categoria;
    private Proveedor proveedor;
}
