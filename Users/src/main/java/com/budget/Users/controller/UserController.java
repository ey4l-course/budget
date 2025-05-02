package com.budget.Users.controller;

import com.budget.Users.LogUtil;
import com.budget.Users.model.User;
import com.budget.Users.model.UserLogin;
import com.budget.Users.security.JwtUtil;
import com.budget.Users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    LogUtil log;
    @PostMapping
    public ResponseEntity<String> register (@RequestBody User user){
        try {
            userService.newUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        }catch (IllegalArgumentException e){
            log.infoGeneral("User registration failure: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            final String uuid = log.debugGeneral(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ref: " + uuid);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody UserLogin userLogin){
        try {
            if (userService.login(userLogin)) {
                log.infoGeneral(String.format("User %s successfully logged in", userLogin.getEmail()));
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            }else {
                log.infoGeneral(String.format("Fail at login. details: %s", userLogin.toString()));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }catch (Exception e){
            final String uuid = log.debugGeneral(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ref: " + uuid);
        }
    }
}
