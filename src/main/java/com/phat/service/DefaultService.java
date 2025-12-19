package com.phat.service;


import com.phat.annotation.Bean;

@Bean
public class DefaultService implements IDefaultService{

    @Override
    public void run(String text){
        System.out.println("Invoked run method: "+ text);
    }

    @Override
    public void defaultMethod() {
        System.out.println("Invoked default method");
    }
}
