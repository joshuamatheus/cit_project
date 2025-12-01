package com.ciandt.nextgen25.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ciandt.nextgen25.security.filter.UserClaimsAuthenticationFilter;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class UserClaimSecurityConfig extends AbstractSecurityConfig {

    @Bean
    @Override
    public OncePerRequestFilter authenticationFilter() {
        return new UserClaimsAuthenticationFilter();
    }
}