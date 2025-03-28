package com.webid.webid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "landing";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "sign_in"; 
    }

    @GetMapping("/auth/register")
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
    @GetMapping("/paynow")
    public String paynow() {
        return "paynow";
    }
    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
    @GetMapping("/reciept")
    public String reciept(){
        return "reciept";
    }

    
    




}
