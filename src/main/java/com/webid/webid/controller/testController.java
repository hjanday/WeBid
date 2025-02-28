package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RestController;

// import com.webid.webid.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class testController {
    @RequestMapping("/test")
    public String test() {
        return "This is a test!";
    }
    
}
