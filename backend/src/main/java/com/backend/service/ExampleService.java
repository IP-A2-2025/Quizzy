package com.backend.service;


import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    public String sayHello() {
        return "Hello World (this is get request)";
    }
    public String sayHi() {
        return "Hi (this is post request)";
    }
}
