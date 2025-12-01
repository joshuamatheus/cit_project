package com.ciandt.nextgen25.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ciandt.nextgen25.security.filter.UserJWTAuthenticationFilter;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class JwtSecurityConfig extends AbstractSecurityConfig {

    @Bean
    @Override
    public OncePerRequestFilter authenticationFilter() {
        return new UserJWTAuthenticationFilter(jwtTokenService());
    }

}