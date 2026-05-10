package com.cavi.stocky.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
// objeto que devolvemos al cliente cuando ocurre un error
// asi controlamos exactamente que informacion recibe, sin exponer detalles tecnicos internos
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private LocalDateTime timestamp; // cuando ocurrio el error
    private int codigo;              // codigo HTTP: 400, 404, 500, etc
    private String titulo;           // titulo corto del error
    private String mensaje;          // descripcion del problema
    private String detalle;          // ruta donde ocurrio el error

}
