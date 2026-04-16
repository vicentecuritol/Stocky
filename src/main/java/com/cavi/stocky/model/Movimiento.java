package com.cavi.stocky.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {
    private Long id;
    private String tipo; 
    private Integer cantidad;
    private LocalDateTime fecha;
    private String observacion;
    
    
    private Producto producto;
}
