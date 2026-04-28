package com.cavi.stocky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponse {
    private long id;
    private String nombre;
    private String descripcion;
}
