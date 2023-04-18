package com.luis.project.cl.users.domain.exception;

import java.io.Serial;

public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4588835396792195132L;

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
