package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.service.registration.RegisterUserService;
import com.skillifyme.auth.Skillify.Me.Auth.service.UserService;
import com.skillifyme.auth.Skillify.Me.Auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;


    @PutMapping("update-username")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        } else {
            String currentUserName = authentication.getName();
            String newUserName = user.get("userName");
            if (newUserName == null) {
                return new ResponseEntity<>("Username can't be empty", HttpStatus.BAD_REQUEST);
            }
            userService.updateUserName(currentUserName, newUserName);
            String newJwt = jwtUtils.generateToken(newUserName);
            Map<String, Object> response = new HashMap<>();
            response.put("new username: ", user);
            response.put("jwt: ", newJwt);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PutMapping("update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            } else {
                String userName = authentication.getName();
                String newPassword = user.get("password");
                userService.updatePassword(userName, newPassword);
                return new ResponseEntity<>("Password has been updated successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("tagy", e);
            return new ResponseEntity<>("Password update failed", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
