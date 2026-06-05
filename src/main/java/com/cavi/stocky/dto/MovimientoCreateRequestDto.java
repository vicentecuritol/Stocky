package com.cavi.stocky.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class MovimientoCreateRequestDto {

    @Pattern(regexp = "^(ENTRADA|SALIDA)$", message = "El tipo debe ser ENTRADA o SALIDA")
    private String tipo;

    @NotNull(message= "La cantidad es obligatoria")
    @Min(value = 1, message= "La cantidad debe ser al menos 1")
    private int cantidad;

    @NotNull(message="La fecha es obligatoria")
    private LocalDateTime fecha;

    private String observacion;

    @NotNull(message= "El id del producto es obligatorio")
    private Long productoId;
}
