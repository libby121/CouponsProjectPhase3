package com.example.demo.jwt;

import com.example.demo.service.AdminDetailsService;
import com.example.demo.service.CompanyDetailsService;
import com.example.demo.service.CustomerDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * security filter, will be executed for requests other than login, register..
 * requests that require authentication and  hold a token for validation
 * after a successful token validation- Authentication object is
 * assembled
 * At the end this filter is pushed to the filterChain
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    private UserDetails userDetails;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private  AdminDetailsService adminDetailsService;
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Override
    //not for login request!!
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token=null;
        String username=null;
        String password=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtService.extractUserName(token);
        }
           if(authHeader!=null &&
                SecurityContextHolder.getContext().
                        getAuthentication()==null){

            String userType = jwtService.extractUserType(token);
              if ("admin".equals(userType)){
                  //fix
                   userDetails =
                   context.getBean(AdminDetailsService.class).loadUserByUsername(username,"1234");
              } else if ("company".equals(userType)) {
                  userDetails =
                          context.getBean(CompanyDetailsService.class). loadUserByUsername(username);
              }
                else if ("customer".equals(userType))
                  userDetails =
                          context.getBean(CompanyDetailsService.class). loadUserByUsername(username);

            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authtoken =
                new UsernamePasswordAuthenticationToken(userDetails,null,
                        userDetails.getAuthorities());
                authtoken.setDetails(new WebAuthenticationDetailsSource().
                        buildDetails(request));

                // update the Authentication Object with authenticated user
                SecurityContextHolder.getContext().setAuthentication(authtoken);
            }

        }

        filterChain.doFilter(request, response);
    }
    }
