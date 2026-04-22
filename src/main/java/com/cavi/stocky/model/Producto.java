    package com.cavi.stocky.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

    @Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    @NotBlank (message = "El nombre no puede estar vacio")
    private String nombre;

    @NotNull (message = "El precio es obligatorio")
    private Integer precio;

    @NotNull (message = "El stock actual es obligatorio")
    @Min(value = 0, message = "El stock actual no puede ser negativo")
    private Integer stockActual;

    @NotNull (message = "El stock minimo es obligatorio")
    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    private Integer stockMinimo;
    
    @ManyToOne
    @JoinColumn (name = "categoria_id")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn (name = "proveedor_id")
    private Proveedor proveedor;
}
