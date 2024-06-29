package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void updateUserName(String currentUserName, String newUserName) {
        User currentUser = userRepository.findByUserName(currentUserName);
        currentUser.setUserName(newUserName);
        userRepository.save(currentUser);
    }

    public void updatePassword(String userName, String newPassword) {
        User currentUser = userRepository.findByUserName(userName);
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }


}
