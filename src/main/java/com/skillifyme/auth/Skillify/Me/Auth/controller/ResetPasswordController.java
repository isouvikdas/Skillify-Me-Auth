package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.service.PasswordService;
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


    @PutMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> user) {
        String email = user.get("email");
        String newPassword = user.get("password");
        try {
            if (newPassword != null) {
                passwordService.resetPassword(email, newPassword);
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
        boolean isVerified = passwordService.verifyOtp(email, otp);
        if (isVerified) {
            return new ResponseEntity<>("Email verified successfully."+ isVerified, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP or OTP expired."+ isVerified, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        if (!email.isEmpty()) {
            boolean isVerified = passwordService.checkEmailVerification(email);
            if (isVerified) {
                passwordService.sendOtpForVerification(email);
                return new ResponseEntity<>("otp has been sent successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No user found with this email", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }
}
