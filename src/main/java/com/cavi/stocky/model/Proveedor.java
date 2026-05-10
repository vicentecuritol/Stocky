package com.cavi.stocky.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// representa la tabla proveedor en la base de datos
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    private String nombre;


    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido") // valida que tenga el formato algo@algo.com
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono; // va como String porque puede tener formato +56 9 1234 5678


}
