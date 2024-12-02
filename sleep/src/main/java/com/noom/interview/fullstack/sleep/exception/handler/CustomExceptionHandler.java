package com.noom.interview.fullstack.sleep.exception.handler;


import com.noom.interview.fullstack.sleep.constant.MessageKey;
import com.noom.interview.fullstack.sleep.exception.BadRequestException;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final ExceptionLogger exceptionLogger;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(RuntimeException e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), null);
        exceptionLogger.logException(e, customizedMessage);
        return customizedMessage;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(NotFoundException e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), e.getArgs());
        LOGGER.error("Exception resource not found: {}", customizedMessage);
        return customizedMessage;
    }
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleThrowable(Throwable e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), null);
        exceptionLogger.logException(e, customizedMessage);
        return customizedMessage;
    }

}
