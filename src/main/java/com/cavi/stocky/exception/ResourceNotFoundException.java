package com.cavi.stocky.exception;
// excepcion propia para cuando no se encuentra un recurso por id
// extiende RuntimeException para no tener que declarar throws en cada metodo
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    } // le pasamos el mensaje al constructor de RuntimeException
}
