package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.repository.InstructorRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Use loadUserByEmailAndType instead");
    }

    @Override
    public UserDetails loadUserByEmailAndType(String email, String userType) throws UsernameNotFoundException {
        AuthUser authUser;
        if (userType.equalsIgnoreCase("USER")) {
            authUser = userRepository.findByEmail(email);
        } else if (userType.equalsIgnoreCase("INSTRUCTOR")) {
            authUser = instructorRepository.findByEmail(email);
        } else {
            throw new UsernameNotFoundException("Invalid user type: " + userType);
        }
        if (authUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new User(authUser.getEmail(), authUser.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByEmail(email);
        if (authUser == null) {
            authUser = instructorRepository.findByEmail(email);
            if (authUser == null) {
                throw new UsernameNotFoundException("Account not found with email: " + email);
            }
        }
        return User.builder()
                .username(authUser.getUserName())
                .password(authUser.getPassword())
                .roles(authUser.getRoles().toArray(new String[0]))
                .build();
    }
}
