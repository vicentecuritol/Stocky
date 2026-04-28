package com.cavi.stocky.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoCreateRequest {
    @NotBlank(message= "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "el precio no puede ser negativo")
    private Integer precio;

    @NotNull(message = "El stock actual es obligatorio")
    @Min(value= 0, message = "El stock actual no puede ser negativo")
    private Integer stockActual;

    @NotNull(message = "El stock minimo es obligatorio")
    @Min(value= 0, message = "El stock minimo no puede ser negativo")
    private Integer stockMinimo;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoriaNombre;

    @NotBlank(message= "El nombre del proveedor es obligatorio")
    private String ProveedorNombre;

    @NotBlank(message = "El email del proveedor es obligatorio")
    @Email(message = "El email del proveedor no es válido")
    private String proveedorEmail;
}


