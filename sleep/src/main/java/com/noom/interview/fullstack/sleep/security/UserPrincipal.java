package com.noom.interview.fullstack.sleep.security;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class UserPrincipal {
    private final int userId;

    private final String name;

    private final String email;
}
