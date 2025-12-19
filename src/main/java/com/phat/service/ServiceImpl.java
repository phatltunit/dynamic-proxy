package com.phat.service;

import com.phat.annotation.Bean;

@Bean
public class ServiceImpl implements IService {
    @Override
    public void run(String name) {
        System.out.println("Invoked: " + name);
    }
}
