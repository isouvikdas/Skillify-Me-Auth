package com.skillifyme.auth.Skillify.Me.Auth.service.registration.user;

import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService {

    @Autowired
    private UserRepository userRepository;


}
