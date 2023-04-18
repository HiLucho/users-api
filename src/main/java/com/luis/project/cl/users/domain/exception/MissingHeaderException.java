package com.luis.project.cl.users.domain.exception;

import java.io.Serial;

/**
 * Service Catalog Exception.
 */
public class MissingHeaderException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 25465102625974935L;

    public MissingHeaderException(String message) {
        super(message);
    }

}
