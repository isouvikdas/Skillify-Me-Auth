package com.skillifyme.auth.Skillify.Me.Auth.controller;

import com.skillifyme.auth.Skillify.Me.Auth.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    @Autowired
    private RegisterService registerService;

//    @GetMapping("all-users")
//    public ResponseEntity<?> getAllUsers() {
//        try {
//            List<User> users =  registerService.getAll();
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("error "+ e);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
