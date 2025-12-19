package com.phat.proxy;

import com.phat.service.IDefaultService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocation implements InvocationHandler {

    private final IDefaultService service;

    public RepositoryInvocation(IDefaultService service){
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{
            System.out.println("Invoked from repository proxy: "+ proxy.getClass());
            service.getClass().getMethod(method.getName(), method.getParameterTypes());
            return method.invoke(service,args);
        } catch (NoSuchMethodException ex){
            System.out.println(ex.getMessage());
            service.defaultMethod();
            return args;
        }
    }
}
