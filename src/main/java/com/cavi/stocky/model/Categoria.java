package com.cavi.stocky.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// representa la tabla categoria en la base de datos
@Data // lombok genera getters, setters, toString y equals automaticamente
@NoArgsConstructor // genera constructor vacio, necesario para que JPA funcione
@AllArgsConstructor // genera constructor con todos los campos
@Entity // le dice a JPA que esta clase es una tabla en la BD
@Table(name = "categoria") // nombre exacto de la tabla en MySQL
public class Categoria {

    @Id // esta campo es la llave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // el id se genera automatico (1, 2, 3...)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;


    private String descripcion; // campo opcional, puede venir vacio
}