package com.hmi.security.service;

import com.hmi.security.model.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class CustomTokenValidator {


    public static final String SIGNING_KEY = "secret";

    public Object validate(String token) throws Exception {

        Claims body = Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
        CustomUser user = new CustomUser();
        user.setUsername(body.getSubject());
        user.setId(Long.parseLong((String) body.get("userId")));
        user.setRole((String) body.get("role"));

        return user;
    }
}
