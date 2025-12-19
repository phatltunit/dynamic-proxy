package com.phat.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CustomInvocation implements InvocationHandler, net.sf.cglib.proxy.InvocationHandler {

    private final Object service;

    public CustomInvocation(Object service){
        this.service = service;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Proxy method has been invoked");
        System.out.println("The actual implementation class: " + proxy.getClass());
        // we can customize the logic if the method name like something then do something else.
        return method.invoke(service, args);
    }
}
