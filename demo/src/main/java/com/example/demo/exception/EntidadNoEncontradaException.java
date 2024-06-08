package com.example.demo.exception;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException() {
    }

    public EntidadNoEncontradaException(String message) {
        super(message);
    }
}
