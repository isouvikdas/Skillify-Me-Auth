package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("create-user")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        if (user != null) {
            user.setDateAndTime(LocalDateTime.now());
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return new ResponseEntity<>("User verified successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired.", HttpStatus.BAD_REQUEST);
        }
    }
}
