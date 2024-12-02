package com.noom.interview.fullstack.sleep.exception;

import com.noom.interview.fullstack.sleep.constant.MessageKey;
import lombok.Getter;

@Getter
public class NotFoundException extends ArgsRuntimeException {

    public NotFoundException() {
        super(MessageKey.ERROR_NOT_FOUND.getName());
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object[] args) {
        super(message, args);
    }
}
