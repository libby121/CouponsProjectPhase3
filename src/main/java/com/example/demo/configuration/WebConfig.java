package com.example.demo.configuration;

import com.example.demo.jwt.JwtFilter;
import com.example.demo.service.CompanyDetailsService;
import com.example.demo.service.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.util.List;

/**
 * Security configuration class.
 * EnableMethodSecurity-enables controlling access at method level
 * for critical operations. (Instead of securing an entire endpoint).
 * Since there are two different databases for each user type(
 * customer, company)- there are two separate AuthenticationProviders
 * each takes a different UserDetailsService class.
 * ProviderManager (implementation of AuthenticationManager takes both
 * providers).
 *
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableJpaAuditing
public class WebConfig {
    private final CompanyDetailsService companyDetailsService;
    private final CustomerDetailsService customerDetailsService;
    private final PasswordConfig passwordConfig;
    private final JwtFilter jwtFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public WebConfig(CustomerDetailsService customerDetailsService,
                     PasswordConfig passwordConfig,
                     CompanyDetailsService companyDetailsService,
                     JwtFilter jwtFilter,
                     CustomAuthenticationProvider customAuthenticationProvider) {
        this.customerDetailsService = customerDetailsService;
        this.passwordConfig = passwordConfig;
        this.companyDetailsService = companyDetailsService;
        this.jwtFilter=jwtFilter;
        this.customAuthenticationProvider=customAuthenticationProvider;

    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.csrf(customizer -> customizer.disable());
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         httpSecurity.
                 authorizeHttpRequests(
                         request ->
                request.
                         requestMatchers(


                                 "/hello/**",
                                 "/login/**"

                         )
                                 .permitAll());
//                        .requestMatchers("/admin/**").hasRole("admin")
//                        .requestMatchers("/company/**").hasRole("admin")
//                        .requestMatchers("/buy/**").hasRole("user")
                     //   .anyRequest().authenticated());
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.authenticationManager(manager());
         httpSecurity.logout(logout -> logout
                 .logoutUrl("/logout").
                  invalidateHttpSession(true)
                 .clearAuthentication(true)
                 .addLogoutHandler(new SecurityContextLogoutHandler())
                 .deleteCookies("JSESSIONID"));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider companyAuthenticationProvider (){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordConfig.passwordEncoder());
        dao.setUserDetailsService(companyDetailsService);
        return dao;
    }

    @Bean
    public AuthenticationProvider customerAuthenticationProvider (){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordConfig.passwordEncoder());
        dao.setUserDetailsService(customerDetailsService);
        return dao;
    }


    @Bean
    public AuthenticationManager manager () {
        try {
//            return new ProviderManager(customAuthenticationProvider);
            return new ProviderManager(List.of(customAuthenticationProvider,companyAuthenticationProvider(),
                    customerAuthenticationProvider()));
        } catch (Exception e) {
            throw new RuntimeException("ProviderManager not initialized ");
        }
    }}

//}
