package com.cavi.stocky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// DTO de respuesta para categoria
// en vez de devolver el modelo de BD directo, usamos esto para controlar que campos ve el cliente
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
}
