package com.noom.interview.fullstack.sleep.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import org.apache.logging.log4j.util.Strings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthFilterTest {

    private final AuthFilter authFilter = new AuthFilter();

    @ParameterizedTest
    @MethodSource("provideArgumentsForStatuses")
    void shouldAuthUser_whenValidateAuthHeader_returnCorrespondingStatus(String authHeader, HttpStatus httpStatus) throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        if (authHeader != null) {
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
        }
        authFilter.doFilter(request, response, chain);

        assertEquals(httpStatus.value(), response.getStatus());
    }


    private static Stream<Arguments> provideArgumentsForStatuses() {
        return Stream.of(Arguments.of(null, HttpStatus.UNAUTHORIZED),
                Arguments.of(Strings.EMPTY, HttpStatus.UNAUTHORIZED),
                Arguments.of("test user id 123", HttpStatus.UNAUTHORIZED),
                Arguments.of(Base64.getEncoder().encodeToString("1a".getBytes(StandardCharsets.UTF_8)), HttpStatus.UNAUTHORIZED),
                Arguments.of(Base64.getEncoder().encodeToString("1".getBytes(StandardCharsets.UTF_8)), HttpStatus.OK),
                Arguments.of(Base64.getEncoder().encodeToString("123".getBytes(StandardCharsets.UTF_8)), HttpStatus.OK));
    }

}
