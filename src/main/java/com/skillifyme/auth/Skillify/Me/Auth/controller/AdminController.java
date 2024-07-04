package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    @Autowired
    private RegisterUserService registerUserService;

    @GetMapping("all-users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users =  registerUserService.getAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("error "+ e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
