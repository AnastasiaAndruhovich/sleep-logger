package com.noom.interview.fullstack.sleep.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageKey {
    ERROR_NOT_FOUND("error.notFound"),
    ERROR_NOT_FOUND_BY_ID("error.notFoundById"),
    ERROR_INTERNAL_SERVER("error.internalServer"),
    ERROR_UNAUTHORIZED("error.unauthorized"),
    ERROR_BAD_REQUEST("error.badRequest");

    private final String name;
}
