package com.example.demo.service;

import com.example.demo.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/** Authentication manager will try to create Authentication object
 ** by looping through the existing providers
 **/
@Service
public class LoginServiceImpl implements LoginService{

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public LoginServiceImpl(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String username, String password,String userType) {
          if (!"admin".equals(userType)
              && !"company".equals(userType)
              && !"customer".equals(userType))
              return "wrong userType :)";

        Authentication authentication =
                authManager.authenticate(new
                        UsernamePasswordAuthenticationToken(username,
                        password));
        if(authentication.isAuthenticated())

            return jwtService.generateToken(username,userType);
        return "User was not authenticated. Wrong " +
                "username or password";

    }
}
