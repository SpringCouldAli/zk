package com.chuan.start.zookeeper.zookeeperdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * author:曲终、人散
 * Date:2019/8/18 22:40
 */
@RestController
public class TestController {


    @Autowired
    RestTemplateBuilder restTemplateBuilder;
//    RestTemplate restTemplate;

    public void test(){
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.put();
    }



}
