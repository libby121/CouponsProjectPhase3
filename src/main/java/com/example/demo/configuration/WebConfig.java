package com.example.demo.configuration;

import com.example.demo.controller.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfig {


    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public SecurityFilterChain filter(HttpSecurity httpsec) throws Exception {
        httpsec.csrf(customizer -> customizer.disable());
        httpsec.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         httpsec.authorizeHttpRequests(requst ->
                requst.requestMatchers("login", "register").permitAll()

                        .anyRequest().authenticated());
         httpsec.authenticationProvider(authenticationProvider());
        httpsec.authenticationProvider(authenticationProvider());
        httpsec.httpBasic(Customizer.withDefaults());


        return httpsec.build();
    }

    /*
    DaoAuthenticatoinProvider(
     a class implementing AuthenticationnProvider) is one of the providers
    for performing authentication by authentication manager.
    this is a db provider.
    since it is a db provider-we need to customize
    the way it will hash the password from db.
    and - the way it will load a user entity.
    (customized UserDetailsService).
     */

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder);
        dao.setUserDetailsService(myUserDetailsService);
        return dao;
    }


}