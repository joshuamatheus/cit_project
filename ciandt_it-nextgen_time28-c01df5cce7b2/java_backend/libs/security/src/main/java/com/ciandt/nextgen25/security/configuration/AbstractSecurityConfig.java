package com.ciandt.nextgen25.security.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ciandt.nextgen25.security.service.JwtTokenService;
public abstract class AbstractSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http))
            .anonymous(anonymous -> anonymous.disable())
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF, habilite conforme necessário
            .authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/by-email" , "/register-password").permitAll() // Rotas públicas
            .anyRequest().authenticated() // Todas as outras rotas requerem autenticação
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
    
        return http.build();
    }
    @Bean
    public abstract OncePerRequestFilter authenticationFilter();
    @Bean
    public AnnotationTemplateExpressionDefaults templateExpressionDefaults() {
        return new AnnotationTemplateExpressionDefaults();
    }
    @Bean 
    public JwtTokenService jwtTokenService() {
        return new JwtTokenService();
    }
}

