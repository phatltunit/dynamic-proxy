package com.phat.service;

import com.phat.annotation.Bean;

@Bean
public class Component {
    public void run(String text){
        System.out.println("Invoked from Component: "+ text);
    }
}
