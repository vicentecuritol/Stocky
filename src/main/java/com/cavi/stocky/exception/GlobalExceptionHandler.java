package com.cavi.stocky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
// atrapa todas las excepciones de todos los controllers
// en vez de mostrar errores tecnicos feos, devuelve un JSON ordenado al cliente
@ControllerAdvice // intercepta las excepciones que escapan de los controllers
public class GlobalExceptionHandler {

        //Respuesta a NoContentException, 204 
        @ExceptionHandler(NoContentException.class)
        public ResponseEntity<ApiError> handleNoContent(NoContentException ex,WebRequest request){
                        ApiError error = new ApiError(
                                LocalDateTime.now(),
                                HttpStatus.NO_CONTENT.value(),
                                "Sin Contenido",
                                ex.getMessage(),
                                request.getDescription(false).replace("uri=", "")
                );
                return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
        }
   
    // se activa cuando lanzamos ResourceNotFoundException, responde 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),          // hora exacta del error
                HttpStatus.NOT_FOUND.value(), // codigo 404
                "Recurso No Encontrado",
                ex.getMessage(),             // mensaje que pusimos al lanzar la excepcion
                request.getDescription(false).replace("uri=", "")  // ruta que fallo
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // se activa cuando lanzamos IllegalArgumentException, responde 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),  // codigo 400
                "Argumento Inválido",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ultimo recurso, atrapa cualquier error inesperado y responde 500
    // el detalle tecnico queda en los logs del servidor, no se expone al cliente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex,
            WebRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),  // codigo 500
                "Error Interno del Servidor",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}