package com.scms.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // Allow access to public pages
        if (requestURI.equals("/") || 
            requestURI.equals("/login") || 
            requestURI.equals("/signup") || 
            requestURI.equals("/forgot-password") ||
            requestURI.startsWith("/api/auth/") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/") ||
            requestURI.startsWith("/swagger-ui/") ||
            requestURI.startsWith("/v3/api-docs/") ||
            requestURI.startsWith("/h2-console/")) {
            return true;
        }
        
        // Check authentication for protected pages
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userType") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        String userType = (String) session.getAttribute("userType");
        
        // Check if user is accessing appropriate pages for their role
        if (requestURI.startsWith("/student/") && !"student".equals(userType)) {
            response.sendRedirect("/login");
            return false;
        }
        
        if (requestURI.startsWith("/admin/") && !"admin".equals(userType)) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}
