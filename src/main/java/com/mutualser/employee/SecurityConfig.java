package com.mutualser.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitamos CSRF para que la consola de H2 pueda enviar datos
                .csrf(csrf -> csrf.disable())

                // 2. Autorizamos las rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Quitamos el 'new AntPathRequestMatcher'
                        .anyRequest().permitAll() // Permitimos todo lo demás por ahora
                )

                // 3. Configuramos los headers para permitir los Frames de H2
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }
}