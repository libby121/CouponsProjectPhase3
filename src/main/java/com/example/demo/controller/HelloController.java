package com.example.demo.controller;

import com.example.demo.mailUtil.EmailService;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
 public class HelloController {
     private final EmailService emailService;
     private final OAuth2AuthorizedClientService authorizedClientService;


    public HelloController(
            EmailService emailService, OAuth2AuthorizedClientService authorizedClientService) {
         this.emailService = emailService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/public")
    public String sayHi(){
        return "hello, this is a public page";
    }
    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal OAuth2User user) {
        return "Hello, " + user.getAttribute("preferred_username");
    }
    @GetMapping("/custom-logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       @AuthenticationPrincipal OidcUser oidcUser) throws IOException {

        if (oidcUser == null) {
            System.out.println("OidcUser is null");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }

        String idToken = oidcUser.getIdToken() != null ? oidcUser.getIdToken().getTokenValue() : null;
        System.out.println("ID Token: " + idToken);  // 🟢 Confirm if this is null or valid

        if (idToken == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No ID token available");
            return;
        }

        String logoutUrl = "http://localhost:8180/realms/spring_coupons/protocol/openid-connect/logout"
                + "?id_token_hint=" + idToken
                + "&post_logout_redirect_uri=http://localhost:8080/public";

        response.sendRedirect(logoutUrl);
    }

    @GetMapping("/claims")
    public Map<String, Object> claims(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object>map=new HashMap<>(oidcUser.getClaims());
        map.put("token",oidcUser.getIdToken());
        return map;
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/forgot/{to}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public ResponseEntity<?>forgotPassword(@PathVariable String to){
        String subject="password renewal";
        String text="to your request..";
        emailService.sendSimpleEmail(to,subject,text);
         return ResponseEntity.ok("email sent successfully");

    }

    @GetMapping("/debug")
    public Map<String, Object> debug(Authentication authentication) {
        return Map.of(
                "authorities", authentication.getAuthorities()
        );
    }

}
