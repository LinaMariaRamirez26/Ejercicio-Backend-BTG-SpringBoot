package com.ejercicio.PruebaTecnica.exception;

public class SinSaldoInvertidoException extends RuntimeException {
    public SinSaldoInvertidoException(String mensaje) {
        super(mensaje);
    }
}