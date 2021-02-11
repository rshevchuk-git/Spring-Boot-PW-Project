package com.bookly.backend.controllers;

import com.bookly.backend.dao.UserRepository;
import com.bookly.backend.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Api(tags = "Users")
@RestController
@AllArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private UserRepository userRepository;

    @GetMapping
    @ApiOperation(value = "Get current users list")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page) {
        Pageable pageConfig = PageRequest.of(page, 5);
        return new ResponseEntity<>(userRepository.findAll(pageConfig), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user info")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<User> getUserInfo(@PathVariable Long id) {
        return new ResponseEntity<>(userRepository.findById(id).orElse(new User()), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Adds new user to the database")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        user.setSecurityToken(UUID.randomUUID().toString());
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

}
