package com.qa.calculadoraapi.exception;

public class DivisionPorCeroException extends RuntimeException {
    public DivisionPorCeroException(String mensaje) {
        super(mensaje);
    }
}