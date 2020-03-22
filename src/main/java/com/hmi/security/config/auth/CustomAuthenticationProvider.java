package com.hmi.security.config.auth;

import com.hmi.security.model.CustomAuthenticationToken;
import com.hmi.security.model.CustomUser;
import com.hmi.security.service.CustomTokenValidator;
import com.hmi.security.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    public CustomTokenValidator validator;


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        return super.authenticate(authentication);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authenticationToken) throws AuthenticationException {

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authenticationToken;
        String token = customAuthenticationToken.getToken();

        CustomUser user;
        try {
            user = (CustomUser) validator.validate(token);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());


        return new CustomUserDetails(user.getUsername(), user.getPassword(), user.getId(), token, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
