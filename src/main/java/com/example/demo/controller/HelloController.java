package com.example.demo.controller;

import com.example.demo.service.AdminService;
import com.example.demo.service.CompanyService;
import com.example.demo.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")

public class HelloController {
    private final LoginService loginService;


    public HelloController(LoginService loginService) {
        this.loginService = loginService;
     }
    @PostMapping("/login/{username}/{password}/{userType}")
    public ResponseEntity<?> login(    @PathVariable String username,
                                       @PathVariable String password,
                                       @PathVariable String userType){
        return ResponseEntity.ok( loginService.login(
                 username,  password, userType));
    }

    //logout
}
