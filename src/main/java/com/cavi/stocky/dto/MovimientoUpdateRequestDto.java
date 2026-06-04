package com.cavi.stocky.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoUpdateRequestDto {
    @NotBlank(message = "Se debe declarar el tipo")
    private String tipo;
    
    @NotNull(message = "Se debe establecer una cantidad")
    @Min(value = 0, message= "El valor no puede ser negativo")
    private Integer cantidad;

    private LocalDateTime fecha;

    @NotBlank(message = "El campo no puede estar vacio")
    private String observacion;

    @NotBlank(message = "Se debe especificar el nombre")
    private String productoNombre; // solo el nombre del producto, no el objeto completo
}
