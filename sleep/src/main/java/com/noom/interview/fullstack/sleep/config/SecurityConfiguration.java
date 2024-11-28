package com.noom.interview.fullstack.sleep.config;

import com.noom.interview.fullstack.sleep.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    public static final String[] ALL_WHITE_LIST = {};

    private final AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorization -> authorization
                        .antMatchers(ALL_WHITE_LIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> jwtFilterRegistration(AuthFilter filter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
