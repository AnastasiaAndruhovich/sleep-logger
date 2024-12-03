package com.noom.interview.fullstack.sleep.filter;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import org.apache.logging.log4j.util.Strings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthFilterTest {

    @Mock
    private UserRepository userRepository;

    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        authFilter = new AuthFilter(userRepository);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForStatuses")
    void shouldAuthUser_whenValidateAuthHeader_returnCorrespondingStatus(String authHeader, HttpStatus httpStatus, Optional<User> user, boolean mockRepository) throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        if (authHeader != null) {
            request.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
        }
        if (mockRepository) {
            when(userRepository.findById(anyLong())).thenReturn(user);
        }

        authFilter.doFilter(request, response, chain);

        assertEquals(httpStatus.value(), response.getStatus());
    }


    private static Stream<Arguments> provideArgumentsForStatuses() {
        return Stream.of(Arguments.of(null, HttpStatus.UNAUTHORIZED, Optional.empty(), false),
                Arguments.of(Strings.EMPTY, HttpStatus.UNAUTHORIZED, Optional.empty(), false),
                Arguments.of("test user id 123", HttpStatus.UNAUTHORIZED, Optional.empty(), false),
                Arguments.of(Base64.getEncoder().encodeToString("1a".getBytes(StandardCharsets.UTF_8)), HttpStatus.UNAUTHORIZED, Optional.empty(), false),
                Arguments.of(Base64.getEncoder().encodeToString("1".getBytes(StandardCharsets.UTF_8)), HttpStatus.UNAUTHORIZED, Optional.empty(), true),
                Arguments.of(Base64.getEncoder().encodeToString("123".getBytes(StandardCharsets.UTF_8)), HttpStatus.OK, Optional.of(new User()), true));
    }

}
