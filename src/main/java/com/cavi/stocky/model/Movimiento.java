package com.cavi.stocky.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
// representa la tabla movimiento, guarda cada entrada o salida de stock
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimiento")

public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^(ENTRADA|SALIDA)$", message = "El tipo debe ser ENTRADA o SALIDA")
    private String tipo; // ENTRADA cuando llega mercaderia, SALIDA cuando se despacha

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha; // guarda fecha y hora exacta del movimiento

    private String observacion; // campo opcional para agregar notas del movimiento

    @ManyToOne  // cada movimiento esta relacionado a un producto
    @JoinColumn (name = "producto_id") // columna en la tabla movimiento con el id del producto
    private Producto producto;
}



