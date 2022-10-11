package com.hau.ketnguyen.it.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;


@Aspect
@Slf4j
@Configuration
public class LoggingAspect {

    @Around("execution(* com.hau.ketnguyen.it.service..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("Enter method " + className + "." + methodName);
        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log.info("Exit method " + className + "." + methodName);
        //Log method execution time
        log.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }
    @Around("execution(* com.hau.ketnguyen.it.repository..*(..)))")
    public Object profileAllMethodsRepository(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("Enter method " + className + "." + methodName);
        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log.info("Exit method " + className + "." + methodName);
        //Log method execution time
        log.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }

    /*@AfterThrowing(value = "execution(* vn.dsoft.inventory.services..*(..)))", throwing = "ex")
    public void afterThrowing(JoinPoint point, Exception ex) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        log.error("Exception in " + className + " in method " + methodName + " with error: " + ex.getMessage());
    }*/
}
