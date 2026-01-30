package com.insett.indicesservice.exceptions;

public class ResourceLoadException extends RuntimeException {
    /**
     * Constructs a ResourceLoadException with the specified detail message and cause.
     *
     * Note: the provided {@code cause} is not recorded as the exception's cause by this constructor.
     *
     * @param message the detail message explaining the error
     * @param cause the underlying cause of the error (not recorded by this constructor)
     */
    public ResourceLoadException(String message, Throwable cause) {
        super(message);
    }
}