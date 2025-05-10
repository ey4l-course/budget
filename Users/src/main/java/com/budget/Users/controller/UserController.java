package com.budget.Users.controller;

import com.budget.Users.LogUtil;
import com.budget.Users.model.AuthResponse;
import com.budget.Users.model.User;
import com.budget.Users.model.UserLogin;
import com.budget.Users.security.JwtUtil;
import com.budget.Users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> login (@RequestBody UserLogin userLogin,
                                         @RequestHeader (value = "X-Forwarded-For", required = false) String xForwarderFor,
                                         HttpServletRequest request){
        try {
            userLogin.setIp((xForwarderFor != null && !xForwarderFor.isEmpty())
                    ? xForwarderFor.split(",")[0].trim()
                    : request.getRemoteAddr());
//            System.out.println("X-forwarder: " + xForwarderFor + "\nrequest.getRemoteAddr()" + request.getRemoteAddr());
            AuthResponse response = userService.login(userLogin);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception e){
            final String uuid = log.debugGeneral(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ref: " + uuid);
        }
    }
}
