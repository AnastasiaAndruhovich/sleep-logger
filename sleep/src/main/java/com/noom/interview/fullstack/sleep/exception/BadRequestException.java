package com.noom.interview.fullstack.sleep.exception;

import com.noom.interview.fullstack.sleep.constant.MessageKey;
import lombok.Getter;

@Getter
public class BadRequestException extends ArgsRuntimeException {
    public BadRequestException() {
        super(MessageKey.ERROR_BAD_REQUEST.getName());
    }

    public BadRequestException(String message) {
        super(message);
    }
}
