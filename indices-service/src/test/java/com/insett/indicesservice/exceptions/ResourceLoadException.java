package com.insett.indicesservice.exceptions;

public class ResourceLoadException extends RuntimeException {
    public ResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
