package com.skillifyme.auth.Skillify.Me.Auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    UserDetails loadUserByEmail(String email);

    UserDetails loadUserByEmailAndType(String email, String userType);
}
