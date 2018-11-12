package com.zy.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zy.order.enums.ResultEnum;
import com.zy.order.exceptions.OrderException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    /**
     * 使用RestTemplate调用其他服务的接口
     * @return
     */
//    @HystrixCommand(fallbackMethod = "fallback")
    //超时配置
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//    })
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
//    })
    @HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList(@RequestParam("number") Integer number){
        if (number % 2 == 0){
            return "success";
        }

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8088/product/listForOrder", Arrays.asList("1"), String.class);
    }

    private String fallback(){
        return "太挤了，稍候";
    }

    private String defaultFallback(){
        return "defaultFallback()";
    }
}
