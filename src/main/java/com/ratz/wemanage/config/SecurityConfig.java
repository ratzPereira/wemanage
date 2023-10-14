package com.ratz.wemanage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {"api/v1/users/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(PUBLIC_URLS).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**").hasAuthority("DELETE:USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/customer/delete/**").hasAuthority("DELETE:CUSTOMER");
        http.exceptionHandling().accessDeniedHandler(null).authenticationEntryPoint(null);
        http.authorizeRequests().anyRequest().authenticated();
        return http.build();
    }

}
