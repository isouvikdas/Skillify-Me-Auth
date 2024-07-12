package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.service.AuthService;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import com.skillifyme.auth.Skillify.Me.Auth.service.UserService;
import com.skillifyme.auth.Skillify.Me.Auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("user")
public class AuthUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;


//    @PutMapping("update-username")
//    public ResponseEntity<?> updateUserName(@RequestBody Map<String, String> payload) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
//        } else {
//            String currentUserName = authentication.getName();
//            String newUserName = payload.get("userName");
//            if (newUserName == null) {
//                return new ResponseEntity<>("Username can't be empty", HttpStatus.BAD_REQUEST);
//            }
//            userService.updateUserName(currentUserName, newUserName);
//            String newJwt = jwtUtils.generateToken(newUserName);
//            Map<String, Object> response = new HashMap<>();
//            response.put("new username: ", payload);
//            response.put("jwt: ", newJwt);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        boolean isVerified = registerService.checkEmailVerification(user.getEmail());
//        if (isVerified) {
//            try{
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//                UserDetails userDetails = authService.loadUserByEmail(user.getEmail());
//                String jwt = jwtUtils.generateToken(userDetails.getUsername());
//                return new ResponseEntity<>(jwt, HttpStatus.OK);
//            }catch (Exception e){
//                log.error("Exception occurred while createAuthenticationToken ", e);
//                return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
//            }
//        } else {
//            return new ResponseEntity<>("Incorrect email id", HttpStatus.UNAUTHORIZED);
//        }
//    }

//    @PutMapping("update-password")
//    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> user) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication == null || !authentication.isAuthenticated()) {
//                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
//            } else {
//                String userName = authentication.getName();
//                String newPassword = user.get("password");
//                userService.updatePassword(userName, newPassword);
//                return new ResponseEntity<>("Password has been updated successfully", HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            log.error("tagy", e);
//            return new ResponseEntity<>("Password update failed", HttpStatus.EXPECTATION_FAILED);
//        }
//    }

}
