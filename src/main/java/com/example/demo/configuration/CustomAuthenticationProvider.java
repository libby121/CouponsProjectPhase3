package com.example.demo.configuration;

import com.example.demo.model.AdminUserDetails;
import com.example.demo.service.AdminDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Instead of validating users by searching in db, a customized provider
 * for admin user
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Value("${sys.username}")
    private String adminUsername;
    @Value("${sys.password}")
    private String adminPassword;
    private final AdminDetailsService adminDetailsService;

    public CustomAuthenticationProvider(AdminDetailsService adminDetailsService) {
        this.adminDetailsService = adminDetailsService;
    }

    @Override
    //for login authentication
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //authentication already exists, has to be verified
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

//        UserDetails details =
//        adminDetailsService.loadUserByUsername(username, password);
//        Authentication authenticated = new UsernamePasswordAuthenticationToken(
//                details, password, details.getAuthorities());
//        //   return authenticated;
//

        if(!adminUsername.equals(username) || !adminPassword.equals(password)){

            throw new BadCredentialsException("not authorized");}

        List<SimpleGrantedAuthority>authorities=List.of(
                (new SimpleGrantedAuthority("ROLE_ADMIN")));
        return new UsernamePasswordAuthenticationToken(username, password,
                authorities);

    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
