package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.service.registration.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private RegisterUserService registerUserService;

    @PutMapping("create-user")
    public ResponseEntity<?> createNewUser(@RequestBody Map<String, String> user) {
        if (user != null) {
            String email = user.get("email");
            String userName = user.get("userName");
            String password = user.get("password");
            if (email.isBlank() || userName.isBlank() || password.isBlank()) {
                return new ResponseEntity<>("Email, username, and password are required", HttpStatus.BAD_REQUEST);
            } else {
                boolean isVerified = registerUserService.checkEmailVerification(email);
                if (isVerified) {
                    registerUserService.saveNewUser(email, userName, password);
                    return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Email not verified", HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String otp = user.get("otp");
        boolean isVerified = registerUserService.verifyOtp(email, otp);
        if (isVerified) {
            return new ResponseEntity<>("User verified successfully."+ true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired."+ false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        if (!email.isEmpty()) {
            registerUserService.sendOtpForVerification(email);
            return new ResponseEntity<>("otp has been sent successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something wrong happened", HttpStatus.BAD_REQUEST);
    }
}
