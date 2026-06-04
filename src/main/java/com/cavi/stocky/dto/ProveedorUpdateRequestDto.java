package com.cavi.stocky.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProveedorUpdateRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "Se debe establecer un email")
    private String email;

    @NotBlank(message = "Se debe establecer un numero de telefono")
    private String telefono;
}
