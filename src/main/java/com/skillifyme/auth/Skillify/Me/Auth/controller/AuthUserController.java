package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.dto.ValidateTokenResponse;
import com.skillifyme.auth.Skillify.Me.Auth.service.AuthService;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import com.skillifyme.auth.Skillify.Me.Auth.service.AuthUserService;
import com.skillifyme.auth.Skillify.Me.Auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("auth")
public class AuthUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private JwtUtils jwtUtils;


    @PutMapping("update-username")
    public ResponseEntity<?> updateUserName(@RequestBody Map<String, String> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newUserName = payload.get("userName");
        String userType = payload.get("userType");

        try {
            authUserService.updateUserName(email, newUserName, userType.toUpperCase());
            return new ResponseEntity<>("Username updated successfully", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newPassword = payload.get("password");
        String userType = payload.get("userType");

        try {
            authUserService.updatePassword(email, newPassword, userType.toUpperCase());
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("validate/instructor")
    public ResponseEntity<ValidateTokenResponse> validateInstructorToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        ValidateTokenResponse response = new ValidateTokenResponse();

        if (jwtUtils.validateToken(token)) {
            String email = jwtUtils.extractEmail(token);

            if (authUserService.validateInstructor(email)) {
                response.setEmail(email);
                response.setValid(true);
                return ResponseEntity.ok(response);
            } else {
                response.setValid(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } else {
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validate/user")
    public ResponseEntity<ValidateTokenResponse> validateUserToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        ValidateTokenResponse response = new ValidateTokenResponse();

        if (jwtUtils.validateToken(token)) {
            String email = jwtUtils.extractEmail(token);

            if (authUserService.validateUser(email)) {
                response.setEmail(email);
                response.setValid(true);
                return ResponseEntity.ok(response);
            } else {
                response.setValid(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } else {
            response.setValid(false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }


}
