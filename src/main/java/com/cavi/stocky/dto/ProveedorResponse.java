package com.cavi.stocky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorResponse {
    private long id;
    private String nombre;
    private String email;
    private String telefono;
}
