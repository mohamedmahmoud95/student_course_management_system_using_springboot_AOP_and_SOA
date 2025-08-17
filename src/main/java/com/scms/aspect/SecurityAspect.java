package com.scms.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);
    
    @Before("execution(* com.scms.controller.*.*(..))")
    public void validateAuthentication(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthenticated access attempt to: {}", joinPoint.getSignature().getName());
            throw new SecurityException("Authentication required");
        }
        
        logger.debug("Authenticated user: {} accessing: {}", 
                    authentication.getName(), joinPoint.getSignature().getName());
    }
    
    @Before("execution(* com.scms.service.AdministratorService.*(..))")
    public void validateAdminAccess(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            logger.warn("Unauthorized admin access attempt by user: {}", authentication.getName());
            throw new SecurityException("Admin privileges required");
        }
        
        logger.debug("Admin access granted to: {} for: {}", 
                    authentication.getName(), joinPoint.getSignature().getName());
    }
}
