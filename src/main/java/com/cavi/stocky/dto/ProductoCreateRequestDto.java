package com.cavi.stocky.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
// DTO de entrada para crear un producto
// el cliente manda los nombres de categoria y proveedor, el controller los busca en la BD
@Data
public class ProductoCreateRequestDto {
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
    private String categoriaNombre;// tiene que coincidir con una categoria ya registrada

    @NotBlank(message= "El nombre del proveedor es obligatorio")
    private String proveedorNombre; // tiene que coincidir con un proveedor ya registrado

    @NotBlank(message = "El email del proveedor es obligatorio")
    @Email(message = "El email del proveedor no es válido")
    private String proveedorEmail; // se valida que coincida con el email registrado del proveedor
}
