package com.study.springsecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.naming.Name;

@Aspect
@Component
@Order(value = 1) // 우선순위
public class TestAspect2 {

    @Pointcut("@annotation(com.study.springsecurity.aspect.annotation.Test2Aop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
//        for(Object obj : proceedingJoinPoint.getArgs()){
//            System.out.println(obj);
//        }
        System.out.println(signature.getName());
        System.out.println(signature.getDeclaringTypeName());

        Object[] args =proceedingJoinPoint.getArgs();
        String[] paramNamess = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            System.out.println(paramNamess[i] + ":" + args[i]);
        }

        System.out.println("전처리2");
        Object result = proceedingJoinPoint.proceed();
        System.out.println("후처리2");

        return result;
    }
}
