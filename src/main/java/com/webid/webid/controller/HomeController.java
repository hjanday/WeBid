package com.webid.webid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "landing";
    }

    @GetMapping("/authentication/login")
    public String login() {
        return "sign_in";
    }

    @GetMapping("/authentication/register")
    public String register() {
        return "sign_up";
    }

    @GetMapping("/authentication/forgotpassword")
    public String forgotpassword() {
        return "forgot_password";
    }

    @GetMapping("auth/auctions")
    public String auctions() {
        return "auctions";
    }

    @GetMapping("auth/createauction")
    public String createauction() {
        return "create_auction";
    }

    @GetMapping("checkout/paynow")
    public String paynow() {
        return "paynow";
    }

    @GetMapping("checkout/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("checkout/reciept")
    public String reciept() {
        return "reciept";
    }

    @GetMapping("admin/dashboard")
    public String admin() {
        return "admin";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
