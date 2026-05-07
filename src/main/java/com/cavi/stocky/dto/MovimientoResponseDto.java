package com.cavi.stocky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoResponseDto {
    private Long id;
    private String tipo;
    private Integer cantidad;
    private LocalDateTime fecha;
    private String observacion;
    private String productoNombre;
}
