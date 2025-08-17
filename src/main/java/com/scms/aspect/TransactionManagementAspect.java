package com.scms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class TransactionManagementAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionManagementAspect.class);
    
    @Around("execution(* com.scms.service.*.save*(..)) || execution(* com.scms.service.*.update*(..)) || execution(* com.scms.service.*.delete*(..))")
    @Transactional
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.debug("Starting transaction for method: {}.{}", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            logger.debug("Transaction committed for method: {}.{}", className, methodName);
            return result;
        } catch (Throwable throwable) {
            logger.error("Transaction rolled back for method: {}.{} due to: {}", 
                        className, methodName, throwable.getMessage());
            throw throwable;
        }
    }
}
