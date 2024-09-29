//package org.chzzk.howmeet.logging.handler;
//
//import lombok.RequiredArgsConstructor;
//import org.chzzk.howmeet.logging.util.ApiQueryCounter;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//@RequiredArgsConstructor
//public class ConnectionInvocationHandler implements InvocationHandler {
//    private final ApiQueryCounter apiQueryCounter;
//    private final Object connection;
//
//    @Override
//    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
//        final Object result = method.invoke(connection, args);
//        if (method.getName().equals("prepareStatement")) {
//            return Proxy.newProxyInstance(
//                    result.getClass().getClassLoader(),
//                    result.getClass().getInterfaces(),
//                    new PreparedStatementInvocationHandler(apiQueryCounter, result)
//            );
//        }
//
//        return result;
//    }
//}
