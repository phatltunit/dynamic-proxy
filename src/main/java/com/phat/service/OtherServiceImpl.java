package com.phat.service;

import com.phat.annotation.Bean;

@Bean
public class OtherServiceImpl implements IOtherService{
    @Override
    public void run(String text) {
        System.out.println("Invoke from OtherService: "+ text);
    }
}
