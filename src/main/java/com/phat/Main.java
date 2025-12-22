package com.phat;


import com.phat.annotation.Bean;
import com.phat.annotation.Repository;
import com.phat.proxy.CustomInvocation;
import com.phat.proxy.RepositoryInvocation;
import com.phat.service.*;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String [] args){
        int num = 20;
        String separator = "=";
        Map<Class<?>, Object> beanFactory = new HashMap<>();
        Reflections reflections = new Reflections(Main.class.getPackage().getName());
        handleByAnnotation(reflections.getTypesAnnotatedWith(Bean.class), beanFactory);
        handleByAnnotation(reflections.getTypesAnnotatedWith(Repository.class), beanFactory);


        IService service = (IService) beanFactory.get(IService.class);
        service.run("IService");
        System.out.println(separator.repeat(num));

        IOtherService service1 = (IOtherService) beanFactory.get(IOtherService.class);
        service1.run("IOtherService");
        System.out.println(separator.repeat(num));
        service1.invokeOtherService(service);
        System.out.println(separator.repeat(num));


        Component com = (Component) beanFactory.get(Component.class);
        com.run("Component");
        System.out.println(separator.repeat(num));
        IServiceWithoutImplementation serviceWithoutImplementation = (IServiceWithoutImplementation) beanFactory.get(IServiceWithoutImplementation.class);
        serviceWithoutImplementation.run("IServiceWithoutImplementation");
        System.out.println(separator.repeat(num));
        serviceWithoutImplementation.test();
        System.out.println(separator.repeat(num));
    }


    static void handleByAnnotation(Collection<Class<?>> classes, Map<Class<?>, Object> beanFactory){
        classes.forEach(clazz -> {
            try {
                // With jdk dynamic proxy
                if(!clazz.isInterface()){
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    if(clazz.getInterfaces().length > 0){
                        Arrays.stream(clazz.getInterfaces()).forEach( inter -> {
                            Object service = new Main().createService(inter, new CustomInvocation(instance));
                            beanFactory.put(inter, service);
                        });
                    }
                    else{ // use cglib
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(clazz);
                        enhancer.setCallback(new CustomInvocation(instance));
                        beanFactory.put(clazz, enhancer.create());
                    }
                }
                else{
                    Object service = new Main().createService(clazz, new RepositoryInvocation(new DefaultService()));
                    beanFactory.put(clazz, service);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    <T> T createService(Class<T> clazz, InvocationHandler invocation){
        return (T) Proxy.newProxyInstance(CustomInvocation.class.getClassLoader(), new Class[]{clazz}, invocation);
    }
}
