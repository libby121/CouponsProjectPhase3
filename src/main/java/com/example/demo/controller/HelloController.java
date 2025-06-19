package com.example.demo.controller;

import com.example.demo.mailUtil.EmailService;
import com.example.demo.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private final LoginService loginService;
    private final EmailService emailService;

    public HelloController(LoginService loginService, EmailService emailService) {
        this.loginService = loginService;
        this.emailService = emailService;
    }
    @GetMapping("/login/{username}/{password}/{userType}")
    public ResponseEntity<?> login(    @PathVariable String username,
                                       @PathVariable String password,
                                       @PathVariable String userType){
        return ResponseEntity.ok( loginService.login(
                 username,  password, userType));
    }

    //logout

    //diff between get and post..
    @GetMapping("/forgot/{to}")
    public ResponseEntity<?>forgotPassword(@PathVariable String to){
        String subject="password renewal";
        String text="to your request..";
        emailService.sendSimpleEmail(to,subject,text);
         return ResponseEntity.ok("email sent successfully");

    }
}
