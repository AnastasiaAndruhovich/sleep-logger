package com.noom.interview.fullstack.sleep.filter;

import com.noom.interview.fullstack.sleep.config.SecurityConfiguration;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.security.CustomAuthenticationToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && !authHeader.isBlank()) {
            try {
                long userId = Long.parseLong(new String(Base64.getDecoder().decode(authHeader), StandardCharsets.UTF_8));
                if (userRepository.findById(userId).isEmpty()) {
                    LOGGER.error("Unauthorized user. User is not found by id {}", userId);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "User is not found");
                } else {
                    LOGGER.info("User id {} invokes method {} {}", userId, request.getMethod(), request.getRequestURI());

                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(new CustomAuthenticationToken(null));
                    filterChain.doFilter(request, response);
                }


            } catch (NumberFormatException e) {
                LOGGER.error("Unauthorized user. Authorization header is not numeric");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is invalid");
            } catch (IllegalArgumentException e) {
                LOGGER.error("Unauthorized user. Authorization header is not Base64 encoded");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is invalid");
            }

        } else {
            LOGGER.error("Unauthorized user. Authorization header is empty");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is empty");
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(SecurityConfiguration.ALL_WHITE_LIST).anyMatch(permittedPath -> new AntPathMatcher().match(permittedPath, path));
    }
}
