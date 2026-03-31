package com.cgsit.training.cdi.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        String className = ctx.getTarget().getClass().getSimpleName();
        String method = ctx.getMethod().getName();
        System.out.println("→ [LOGGED] " + className + "." + method + "() called");

        long start = System.currentTimeMillis();
        Object result = ctx.proceed();
        long duration = System.currentTimeMillis() - start;

        System.out.println("← [LOGGED] " + className + "." + method + "() finished (" + duration + " ms)");
        return result;
    }
}
