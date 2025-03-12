package com.webid.webid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "landing";
    }

    @GetMapping("/login")
    public String login() {
        return "sign_in";
    }

    @GetMapping("/register")
    public String register() {
        return "sign_up";
    }

    @GetMapping("/forgotpassword")
    public String forgotpassword() {
        return "forgot_password";
    }

    @GetMapping("/auctions")
    public String auctions() {
        return "auctions";
    }

    @GetMapping("/createauction")
    public String createauction() {
        return "create_auction";
    }

}
