package com.luis.project.cl.users.infrastructure.adapter.in.web;


import com.luis.project.cl.users.domain.exception.UserAlreadyExistsException;
import com.luis.project.cl.users.domain.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ProblemDetail handleConstraintViolationException(Exception ex) {
        log.error(ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ProblemDetail handleArgumentException(MethodArgumentNotValidException ex) {
        log.error(String.valueOf(ex.getBindingResult()));
        var errors = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ProblemDetail handleUserNotFoundException(UserNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handle all Exception without being handled by internal
     */
    @ExceptionHandler({Exception.class})
    public ProblemDetail handle(Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
