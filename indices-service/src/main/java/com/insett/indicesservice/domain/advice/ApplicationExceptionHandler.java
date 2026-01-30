package com.insett.indicesservice.domain.advice;

import com.insett.indicesservice.domain.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Produces a ProblemDetail representing an HTTP 400 Bad Request for the given BadRequestException.
     *
     * @param ex the BadRequestException that triggered this handler
     * @return a ProblemDetail with HTTP status 400 and the exception's message as the detail
     */
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}