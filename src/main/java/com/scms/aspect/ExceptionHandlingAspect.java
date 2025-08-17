package com.scms.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);
    
    @AfterThrowing(pointcut = "execution(* com.scms.service.*.*(..))", throwing = "error")
    public void handleServiceException(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.error("Exception in {}.{}: {}", className, methodName, error.getMessage());
        
        if (error instanceof RuntimeException) {
            logger.error("Runtime exception details: ", error);
        } else {
            logger.error("Checked exception details: ", error);
        }
    }
    
    @AfterThrowing(pointcut = "execution(* com.scms.controller.*.*(..))", throwing = "error")
    public void handleControllerException(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.error("Controller exception in {}.{}: {}", className, methodName, error.getMessage());
        
        if (error instanceof SecurityException) {
            logger.warn("Security violation in {}.{}: {}", className, methodName, error.getMessage());
        }
    }
}
