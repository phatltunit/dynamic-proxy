package com.phat;


import com.phat.annotation.Bean;
import com.phat.proxy.CustomInvocation;
import com.phat.service.Component;
import com.phat.service.IOtherService;
import com.phat.service.IService;
import com.phat.service.ServiceImpl;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String [] args){

        Map<Class<?>, Object> beanFactory = new HashMap<>();
        Reflections reflections = new Reflections(Main.class.getPackage().getName());
        reflections.getTypesAnnotatedWith(Bean.class).forEach(clazz -> {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                // With jdk dynamic proxy
                if(clazz.getInterfaces().length > 0){
                    Arrays.stream(clazz.getInterfaces()).forEach( inter -> {
                        Object service = new Main().createService(inter, instance);
                        beanFactory.put(inter, service);
                    });
                }
                else{ // use cglib
                    Enhancer enhancer = new Enhancer();
                    enhancer.setSuperclass(clazz);
                    enhancer.setCallback(new CustomInvocation(instance));
                    beanFactory.put(clazz, enhancer.create());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        IService service = (IService) beanFactory.get(IService.class);
        service.run("Name 2");
        IOtherService service1 = (IOtherService) beanFactory.get(IOtherService.class);
        service1.run("Text2");
        Component com = (Component) beanFactory.get(Component.class);
        com.run("Com");
    }



    @SuppressWarnings("unchecked")
    <T> T createService(Class<T> clazz, Object object){
        return (T) Proxy.newProxyInstance(CustomInvocation.class.getClassLoader(), new Class[]{clazz}, new CustomInvocation(object));
    }
}
