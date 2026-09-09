package com.interviewcoach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Para nuestra API REST durante esta etapa.
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Toda nuestra API queda accesible temporalmente.
                        .requestMatchers("/api/**").permitAll()

                        // Cualquier otra ruta también.
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}