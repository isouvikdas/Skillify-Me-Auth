package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterService registerService;

    @PutMapping("signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> user) {
        if (user != null) {
            String email = user.get("email");
            String userName = user.get("userName");
            String password = user.get("password");
            String userType = user.get("userType");
            if (email.isBlank() || userName.isBlank() || password.isBlank()) {
                return new ResponseEntity<>("Email, username, and password are required", HttpStatus.BAD_REQUEST);
            } else {
                boolean isVerified = registerService.checkEmailVerification(email);
                if (isVerified) {
                    if (userType.equalsIgnoreCase("USER")) {
                        registerService.saveNewUser(email, userName, password, User.class);
                    } else  {
                        registerService.saveNewUser(email, userName, password, Instructor.class);
                    }
                    return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Email not verified", HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String otp = user.get("otp");
        boolean isVerified = registerService.verifyOtp(email, otp);
        if (isVerified) {
            return new ResponseEntity<>("Email verified successfully."+ isVerified, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired."+ isVerified, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String userType = payload.get("userType");
        if (!email.isEmpty() && !userType.isEmpty()) {
            registerService.sendOtpForVerification(email, userType);
            return new ResponseEntity<>("otp has been sent successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something wrong happened", HttpStatus.BAD_REQUEST);
    }
}

