package com.ratz.wemanage.config;

import com.ratz.wemanage.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {"api/v1/users/**"};

    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(PUBLIC_URLS).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**").hasAuthority("DELETE:USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/customer/delete/**").hasAuthority("DELETE:CUSTOMER");
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(null);
        http.authorizeRequests().anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(null);

        return new ProviderManager(provider);
    }

}
