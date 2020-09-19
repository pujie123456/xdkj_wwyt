//package com.jib.wwyt.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * @classname：LogAspect
// * @author: zuozhuopei
// * @time: 2020/4/26 10:16
// * @version：1.0
// */
//@Component
//@Aspect
//@Slf4j
//public class LogAspect {
//
////    private static final Logger logger = LogManager.getLogger(LogAspect.class);
//
//    @Before("execution(* com.jib.wwyt.controller..*.*(..)) && !execution(* com.jib.wwyt.controller.ImageUpdateController.*(..))")
//    public void before(JoinPoint joinPoint){
//        Object[] args = joinPoint.getArgs();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        log.info("{}.{} : 请求参数：{}",method.getDeclaringClass().getName(),method.getName(), StringUtils.join(args,","));
//
//    }
//
//
//
//    @AfterReturning(value = "execution(* com.jib.wwyt.controller..*.*(..))",returning = "rvt")
//    public void after(JoinPoint joinPoint,Object rvt){
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        log.info("{}.{}: 返回数据: {}",method.getDeclaringClass().getName(),method.getName(), rvt);
//    }
//}
