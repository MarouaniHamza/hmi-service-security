package com.hmi.security.controller;

import com.hmi.security.model.CustomUser;
import com.hmi.security.service.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class SecurityController {

    @Autowired
    public TokenGenerator tokenGenerator;

    @Autowired
    public AuthenticationManager authenticationManager;

    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> createJwtToken(@RequestBody CustomUser user) throws Exception {

        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassWord()));


        return ResponseEntity.ok(tokenGenerator.generate(user));
    }
}
