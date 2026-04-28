package com.cavi.stocky.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {
    private long id;
    private String nombre;
    private Integer precio;
    private Integer stockActual;
    private Integer stockMinimo;
    private String categoriaNombre;
    private String proveedorNombre;
}
