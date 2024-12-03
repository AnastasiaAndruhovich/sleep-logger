package com.noom.interview.fullstack.sleep.exception;

import com.noom.interview.fullstack.sleep.constant.MessageKey;

public class ConflictRequestException extends ArgsRuntimeException {

    public ConflictRequestException() {
        super(MessageKey.ERROR_CONFLICT.getName());
    }

    public ConflictRequestException(String message, Object[] args) {
        super(message, args);
    }

}
