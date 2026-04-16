package com.cavi.stocky.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
}
