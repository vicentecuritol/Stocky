package com.cavi.stocky.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "movimiento")

public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //private Long id;
    private String tipo; 
    private Integer cantidad;
    private LocalDateTime fecha;
    private String observacion;
    
    
    private Producto producto;
}
