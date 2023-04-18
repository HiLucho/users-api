package com.luis.project.cl.users.domain.exception;

import java.io.Serial;

/**
 * Your API Exception.
 */
public class UserApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -701398297529484583L;

    public UserApiException(String message) {
        super(message);
    }

}
