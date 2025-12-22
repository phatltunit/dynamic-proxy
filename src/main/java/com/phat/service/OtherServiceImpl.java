package com.phat.service;

import com.phat.annotation.Bean;

@Bean
public class OtherServiceImpl implements IOtherService{
    @Override
    public void run(String text) {
        System.out.println("OtherServiceImpl.run("+ text +")");
    }

    @Override
    public void invokeOtherService(IService service) {
        System.out.println("OtherServiceImpl.invokeOtherService(" + service.getClass() +")");
        service.runWithoutProxy();
    }
}
