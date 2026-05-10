    package com.cavi.stocky.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    // representa la tabla producto, es la entidad principal del inventario
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    @NotBlank (message = "El nombre no puede estar vacio")
    private String nombre;

    @NotNull (message = "El precio es obligatorio") // NotNull porque es numero, no String
    private Integer precio;

    @NotNull (message = "El stock actual es obligatorio")
    @Min(value = 0, message = "El stock actual no puede ser negativo")
    private Integer stockActual; // cuantas unidades hay en este momento

    @NotNull (message = "El stock minimo es obligatorio")
    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    private Integer stockMinimo; // alerta cuando el stock actual baje de este numero

    @ManyToOne// muchos productos pueden pertenecer a una categoria
    @JoinColumn (name = "categoria_id", nullable = false) // columna en la tabla producto que guarda el id de la categoria
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    @ManyToOne  // muchos productos pueden tener el mismo proveedor
    @JoinColumn (name = "proveedor_id", nullable = false) // columna en la tabla producto que guarda el id del proveedor
    @NotNull(message = "El proveedor es obligatorio")
    private Proveedor proveedor;

}
