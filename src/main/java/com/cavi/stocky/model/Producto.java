package com.cavi.stocky.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String nombre;
    private Integer precio;
    private Integer stockActual;
    private Integer stockMinimo;
    
    // Relaciones simples como objetos
    private Categoria categoria;
    private Proveedor proveedor;
}
