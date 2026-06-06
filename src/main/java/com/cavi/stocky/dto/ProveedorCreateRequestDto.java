package com.cavi.stocky.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProveedorCreateRequestDto {

    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
}