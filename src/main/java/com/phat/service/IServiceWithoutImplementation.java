package com.phat.service;

import com.phat.annotation.Repository;

@Repository
public interface IServiceWithoutImplementation extends IDefaultService {

    void test();

}
