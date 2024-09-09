package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.InstructorRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateUserName(String email, String newUserName, String userType) {
        AuthUser authUser = registerService.findUserByEmailAndType(email, userType);
        if (authUser != null) {
            authUser.setUserName(newUserName);
            if (userType.equalsIgnoreCase("USER")) {
                userRepository.save((User) authUser);
            } else {
                instructorRepository.save((Instructor) authUser);
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void updatePassword(String email, String newPassword, String userType) {
        AuthUser authUser = registerService.findUserByEmailAndType(email, userType);
        if (authUser != null) {
            authUser.setPassword(passwordEncoder.encode(newPassword));
            if (userType.equalsIgnoreCase("USER")) {
                userRepository.save((User) authUser);
            } else {
                instructorRepository.save((Instructor) authUser);
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public boolean validateInstructor(String email) {
        AuthUser instructor = instructorRepository.findByEmail(email);
        return instructor != null && instructor.isVerified();
    }

    public boolean validateUser(String email) {
        AuthUser user = userRepository.findByEmail(email);
        return user != null && user.isVerified();
    }
}
