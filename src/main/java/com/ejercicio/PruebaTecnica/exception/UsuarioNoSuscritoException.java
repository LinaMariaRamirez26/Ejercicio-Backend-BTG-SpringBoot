package com.ejercicio.PruebaTecnica.exception;

public class UsuarioNoSuscritoException extends RuntimeException {
    public UsuarioNoSuscritoException(String mensaje) {
        super(mensaje);
    }
}
