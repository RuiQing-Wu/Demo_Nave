package com.example.demo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{
	private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    // Define a pointcut that matches all methods in the service package NaveService
    @Pointcut("execution(* com.example.demo.service.NaveService.*(..))")
    public void pointcut1() {
    }

    @Pointcut("@annotation(com.example.demo.utils.LogExecution)")
    public void pointcut2() {
    }

    // Execution method point before
    @Before(value = "pointcut1()")
    public void before(JoinPoint joinPoint) {
        LOGGER.info("Execution method of " + joinPoint.getSignature());
    }

    @Before(value = "pointcut2()")
    public void before2(JoinPoint joinPoint) {
        LOGGER.info("Execution method of cut 2: " + joinPoint.getSignature());
    }

    // Execution method point after throwing exception
    @AfterThrowing(value = "pointcut2()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        //发生异常之后输出异常信息
        LOGGER.error("Exception when " + joinPoint.getSignature() + " with exception message: " + e.getMessage());
    }
}