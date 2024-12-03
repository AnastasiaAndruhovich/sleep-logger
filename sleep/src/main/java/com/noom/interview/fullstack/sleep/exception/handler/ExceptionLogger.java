package com.noom.interview.fullstack.sleep.exception.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);

    private final MessageSource messageSource;

    public void logException(Throwable e, String customized) {
        if (e.getMessage().equals(customized)) {
            LOGGER.error("Exception: ", e);
        } else {
            LOGGER.error("Exception with customized message: {}", customized, e);
        }
    }

    public String getMessageByParams(String key, Object[] args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }
}
