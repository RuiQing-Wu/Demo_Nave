package com.example.demo.exception;

public class RepositoryException extends RuntimeException{
    public RepositoryException() {
    }
    
    public RepositoryException(String message) {
        super(message);
    }
}
