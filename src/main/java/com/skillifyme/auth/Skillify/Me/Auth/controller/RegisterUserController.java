package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.service.UserDetailsServiceImpl;
import com.skillifyme.auth.Skillify.Me.Auth.service.registration.RegisterUserService;
import com.skillifyme.auth.Skillify.Me.Auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("register")
public class RegisterUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private RegisterUserService registerUserService;

    @PutMapping("signup")
    public ResponseEntity<?> signupUser(@RequestBody Map<String, String> user) {
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

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        boolean isVerified = registerUserService.checkEmailVerification(user.getEmail());
        if (isVerified) {
            try{
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
                UserDetails userDetails = userDetailsService.loadUserByEmail(user.getEmail());
                String jwt = jwtUtil.generateToken(userDetails.getUsername());
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            }catch (Exception e){
                log.error("Exception occurred while createAuthenticationToken ", e);
                return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Incorrect email id", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String otp = user.get("otp");
        boolean isVerified = registerUserService.verifyOtp(email, otp);
        if (isVerified) {
            return new ResponseEntity<>("Email verified successfully."+ isVerified, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired."+ isVerified, HttpStatus.BAD_REQUEST);
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
