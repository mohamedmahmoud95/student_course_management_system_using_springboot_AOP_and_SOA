package com.scms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/signup", "/forgot-password", 
                                   "/api/auth/**", "/api/students/**", "/api/courses/**", 
                                   "/api/enrollments/**", "/api/grades/**", "/api/administrators/**", 
                                   "/api/notifications/**", "/css/**", "/js/**", "/images/**",
                                   "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**");
    }
}
