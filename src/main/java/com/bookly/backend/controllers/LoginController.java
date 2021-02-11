package com.bookly.backend.controllers;

import com.bookly.backend.models.Credentials;
import com.bookly.backend.models.Token;
import com.bookly.backend.models.User;
import com.bookly.backend.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Login")
@RestController
@AllArgsConstructor
@RequestMapping("/v1/login")
public class LoginController {

    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "Login to the system")
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        User user = userService.tryGetUserByLogin(credentials.getLogin()).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(6);
            if (encoder.matches(user.getPassword(), credentials.getPassword())) {
                return new ResponseEntity<>(new Token(user.getId(), user.getSecurityToken()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
