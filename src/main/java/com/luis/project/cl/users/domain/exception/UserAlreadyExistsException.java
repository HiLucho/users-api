package com.luis.project.cl.users.domain.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2902328428411204926L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
