package org.chzzk.howmeet.logging.handler;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.logging.util.ApiQueryCounter;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class PreparedStatementInvocationHandler implements InvocationHandler {
    private final ApiQueryCounter apiQueryCounter;
    private final Object preparedStatement;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.getName().contains("execute") && RequestContextHolder.getRequestAttributes() != null) {
            apiQueryCounter.increaseCount();
        }

        return method.invoke(preparedStatement, args);
    }
}
