package com.cybertek.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class PerformanceAspect {
@Pointcut("@annotation(com.cybertek.annotation.ExecutionTimeAnnotation)")
private void anyExecutionTimeAnnotation(){
}
@Around("anyExecutionTimeAnnotation()")
    public Object anyExecutionOperationAdvice(ProceedingJoinPoint proceedingJoinPoint){
    long beforeTime= System.currentTimeMillis();
    Object result=null;
    try {

        result=proceedingJoinPoint.proceed();
    }catch (Throwable throwable){
        throwable.printStackTrace();
    }
    long afterTime =System.currentTimeMillis();
    log.info("Time taken to execute ", (afterTime-beforeTime), proceedingJoinPoint.getSignature(),proceedingJoinPoint.getArgs());
return result;
}
}
