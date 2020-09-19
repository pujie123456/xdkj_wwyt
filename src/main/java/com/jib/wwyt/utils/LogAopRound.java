//package com.jib.wwyt.utils;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.Enumeration;
//
//@Component
//@Aspect
//public class LogAopRound {
//
//    private static final Logger LOGGER = LogManager.getLogger(LogAopRound.class);
//
//    private static final String exceedingly = "程序异常";
//
//    @Around("execution(* com.jib.wwyt.controller..*.*(..)) && !execution(* com.jib.wwyt.controller.ImageUpdateController.*(..))")
//    public Object aopLog(ProceedingJoinPoint p) {
//        long time = System.currentTimeMillis();
//        Object s = null;
//        try {
//            s = p.proceed();
//            log(p, s, time, null);
//        } catch (Throwable e) {
//            log(p, s, time, e);
//            return exceedingly;
//        }
//        return s;
//    }
//
//    public void log(ProceedingJoinPoint jp, Object rvt, long time, Throwable e1) {
//        try {
//            Object[] args = jp.getArgs();
//            if (args == null || args.length < 1) {
//                return;
//            }
//            StringBuilder s = new StringBuilder();
//            String uri = null;
//            if (args.length > 1 && args[1] instanceof HttpServletRequest) {
//                HttpServletRequest h = (HttpServletRequest) args[1];
//                uri = h.getRequestURI();
//                s.append(uri).append("?").append(h.getQueryString()).append(" ").append(args[0]);
//                s.append(" FRM:").append(h.getRemoteAddr());
//            } else if (args.length > 0 && args[0] instanceof HttpServletRequest) {
//                HttpServletRequest h = (HttpServletRequest) args[0];
//                uri = h.getRequestURI();
//                s.append(uri).append("?").append(h.getQueryString());
//
//                Enumeration<String> pns = h.getParameterNames();
//                if (pns != null) {
//                    while (pns.hasMoreElements()) {
//                        String pn = pns.nextElement();
//                        s.append(" ").append(pn).append("=").append(Arrays.toString(h.getParameterValues(pn)));
//                    }
//                }
//                s.append(" FRM:").append(h.getRemoteAddr());
//            }else{
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                HttpServletRequest h = attributes.getRequest();
//                s.append(h.getRequestURI()).append("?").append(Arrays.toString(args));
//                s.append(" FRM:").append(h.getRemoteAddr());
//            }
//            long spend = System.currentTimeMillis() - time;
//            s.append(" RTN:");
//            if (e1 != null) {
//                s.append(e1.getMessage()).append(" SPD:").append(spend);
//                LOGGER.error(s.toString(),e1);
//            } else {
//                Object dr = null;
//                if (rvt != null && rvt.toString().length() > 1000) {
//                    dr = rvt.toString().substring(0, 1000);
//                } else {
//                    dr = rvt;
//                }
//                s.append(dr).append(" SPD:").append(spend);
//                LOGGER.info(s.toString());
//            }
////            logStatWriter.put(new Log(uri, rvt, spend));
//        } catch (Throwable e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//}
