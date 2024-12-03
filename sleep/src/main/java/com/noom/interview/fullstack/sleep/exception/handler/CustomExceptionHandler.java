package com.noom.interview.fullstack.sleep.exception.handler;


import com.noom.interview.fullstack.sleep.exception.BadRequestException;
import com.noom.interview.fullstack.sleep.exception.ConflictRequestException;
import com.noom.interview.fullstack.sleep.exception.NotFoundException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final ExceptionLogger exceptionLogger;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        LOGGER.error("Bad request exception: {}", message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleJsonParseException(HttpMessageNotReadableException ex) {
        LOGGER.error("Bad request exception: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    public String handleIllegalArgumentException(RuntimeException e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), null);
        exceptionLogger.logException(e, customizedMessage);
        return customizedMessage;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), e.getArgs());
        LOGGER.error("Resource not found exception : {}", customizedMessage);
        return customizedMessage;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictRequestException.class)
    public String handleNotFoundException(ConflictRequestException e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), e.getArgs());
        LOGGER.error("Conflict exception: {}", customizedMessage);
        return customizedMessage;
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThrowable(Throwable e) {
        String customizedMessage = exceptionLogger.getMessageByParams(e.getMessage(), null);
        exceptionLogger.logException(e, customizedMessage);
        return customizedMessage;
    }

}
