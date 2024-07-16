package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.service.AuthService;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import com.skillifyme.auth.Skillify.Me.Auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

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
                boolean isVerified = registerService.checkEmailVerificationForCreation(email, userType);
                if (isVerified) {
                    registerService.saveNewUser(email, userName, password, userType.toUpperCase());
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
        String userType = user.get("userType");
        boolean isVerified = registerService.verifyOtp(email, otp, userType.toUpperCase());
        if (isVerified) {
            return new ResponseEntity<>("Email verified successfully." + isVerified, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired." + isVerified, HttpStatus.BAD_REQUEST);
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

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        String userType = payload.get("userType");
        if (email.isBlank() || password.isBlank() || userType.isBlank()) {
            return new ResponseEntity<>("email, password & userType is required", HttpStatus.BAD_REQUEST);
        }
        boolean isVerified = registerService.checkEmailVerification(email, userType);
        if (isVerified) {
            try {
                AuthUser authUser = registerService.findUserByEmailAndType(email, userType);
                if (authUser != null && passwordEncoder.matches(password, authUser.getPassword())) {
                    String jwt = jwtUtils.generateToken(email);
                    return new ResponseEntity<>(jwt, HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("Exception occurred while createAuthenticationToken ", e);
                return new ResponseEntity<>("Incorrect email or password", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Email not verified", HttpStatus.UNAUTHORIZED);
    }

}

