package com.example.demo.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final JwtDecoder jwtDecoder;

    public CustomOidcUserService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        // Decode access token
        String accessToken = userRequest.getAccessToken().getTokenValue();
        Jwt jwt = jwtDecoder.decode(accessToken);

        // Extract custom_roles
        List<String> roles = jwt.getClaimAsStringList("custom_roles");
        if (roles == null) {
            roles = Collections.emptyList();
        }

        // Map to Spring authorities
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        for (String role : roles) {
            mappedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        // Combine with existing authorities
        mappedAuthorities.addAll(oidcUser.getAuthorities());

        // Return new user object with updated authorities
        return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
