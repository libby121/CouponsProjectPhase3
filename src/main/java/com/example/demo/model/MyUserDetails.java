package com.example.demo.model;

import com.example.demo.entity.Company;
import com.example.demo.entity.Customer;
import com.example.demo.entity.MyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
  public class MyUserDetails implements UserDetails {

    private MyUser myUser;


    public MyUserDetails(MyUser myUser) {
        this.myUser = myUser;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(myUser instanceof Company company  )
            return company.getRoles()
                    .stream().map(role->
                            new SimpleGrantedAuthority("ROLE_"+role.getRoleName()))
                    .collect(Collectors.toSet());

        return ((Customer) myUser).getRoles()
                .stream().map(role ->
                        new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return myUser.getPassword();
    }

    @Override
    public String getUsername() {
        return myUser.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
