package com.ejercicio.PruebaTecnica.exception;

public class FondoNoEncontradoException extends RuntimeException {
    public FondoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}