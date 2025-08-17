package com.scms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/register", "/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/swagger-resources/**", "/webjars/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/student/**", "/admin/**").permitAll()
                .requestMatchers("/api/students/**").permitAll()
                .requestMatchers("/api/courses/**").permitAll()
                .requestMatchers("/api/enrollments/**").permitAll()
                .requestMatchers("/api/grades/**").permitAll()
                .requestMatchers("/api/administrators/**").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/**", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**")
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );
        
        return http.build();
    }
}
