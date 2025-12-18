package com.anuj.algotracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/")
    public String getMethodName() {
        return new String("hello world!!!");
    }
    
}
