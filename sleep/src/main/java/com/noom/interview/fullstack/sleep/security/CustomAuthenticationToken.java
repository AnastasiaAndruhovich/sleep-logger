package com.noom.interview.fullstack.sleep.security;

import java.util.ArrayList;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal userPrincipal;

    public CustomAuthenticationToken(UserPrincipal userPrincipal) {
        super(new ArrayList<>());
        setAuthenticated(true);

        this.userPrincipal = userPrincipal;
    }

    @Override
    public Object getCredentials() {
        return Strings.EMPTY;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }
}
