package com.cavi.stocky.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimiento")

public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; 
    private Integer cantidad;
    private LocalDateTime fecha;
    private String observacion;
    
    @ManyToOne
    @JoinColumn (name = "producto_id")
    private Producto producto;
}
