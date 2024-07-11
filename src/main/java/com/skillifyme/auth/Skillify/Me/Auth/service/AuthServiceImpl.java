package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.repository.InstructorRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByUserName(userName);
        if (authUser == null) {
            authUser = instructorRepository.findByUserName(userName);
            if (authUser == null) {
                throw new UsernameNotFoundException("User not found with username: "+ userName);
            }
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(authUser.getUserName())
                .password(authUser.getPassword())
                .roles(authUser.getRoles().toArray(new String[0]))
                .build();
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
