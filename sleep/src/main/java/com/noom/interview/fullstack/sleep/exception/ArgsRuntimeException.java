package com.noom.interview.fullstack.sleep.exception;

import lombok.Getter;

@Getter
public class ArgsRuntimeException extends RuntimeException {
    private Object[] args;

    public ArgsRuntimeException() {
    }

    public ArgsRuntimeException(String message) {
        super(message);
    }

    public ArgsRuntimeException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public ArgsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgsRuntimeException(String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.args = args;
    }
}
