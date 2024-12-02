package com.noom.interview.fullstack.sleep.exception;

import com.noom.interview.fullstack.sleep.constant.MessageKey;

public class InternalServerException extends ArgsRuntimeException {

    private static final String DEFAULT_MESSAGE = MessageKey.ERROR_INTERNAL_SERVER.getName();

    public InternalServerException() {
        super(DEFAULT_MESSAGE);
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Object[] args) {
        super(message, args);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
