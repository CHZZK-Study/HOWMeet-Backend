//package org.chzzk.howmeet.logging;
//
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.chzzk.howmeet.logging.handler.ConnectionInvocationHandler;
//import org.chzzk.howmeet.logging.util.ApiQueryCounter;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Proxy;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class ApiQueryCounterAspect {
//    private final ApiQueryCounter apiQueryCounter;
//
//    @Around("execution(* javax.sql.DataSource.getConnection())")
//    public Object getConnection(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        final Object connection = proceedingJoinPoint.proceed();
//        return Proxy.newProxyInstance(
//                connection.getClass().getClassLoader(),
//                connection.getClass().getInterfaces(),
//                new ConnectionInvocationHandler(apiQueryCounter, connection)
//        );
//    }
//}
