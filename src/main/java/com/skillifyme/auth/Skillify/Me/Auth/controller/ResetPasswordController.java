package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.service.PasswordService;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("password")
@Slf4j
public class ResetPasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private RegisterService registerService;

    @PutMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("password");
        String userType = payload.get("userType");
        try {
            if (newPassword != null) {
                passwordService.resetPassword(email, newPassword, userType.toUpperCase());
                return new ResponseEntity<>("Password has been successfully reset", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Password can't be empty", HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("error", e);
            return new ResponseEntity<>("Password reset failed", HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PutMapping("verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String otp = user.get("otp");
        String userType = user.get("userType");
        boolean isVerified = passwordService.verifyOtp(email, otp, userType.toUpperCase());
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
            boolean isVerified = registerService.checkEmailVerification(email, userType);
            if (isVerified) {
                passwordService.sendOtpForVerification(email, userType.toUpperCase());
                return new ResponseEntity<>("otp has been sent successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No user found with this email", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }
}
