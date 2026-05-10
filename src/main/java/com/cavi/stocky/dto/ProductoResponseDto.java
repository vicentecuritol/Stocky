package com.cavi.stocky.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// DTO de respuesta para producto
// mostramos solo los nombres de categoria y proveedor, no los objetos completos
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDto {
    private Long id;
    private String nombre;
    private Integer precio;
    private Integer stockActual;
    private Integer stockMinimo;
    private String categoriaNombre; // nombre de la categoria en vez del objeto completo
    private String proveedorNombre; // nombre del proveedor en vez del objeto completo
}
