package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import com.skillifyme.auth.Skillify.Me.Auth.utils.GenerateOTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenerateOTP generateOTP;

    @Autowired
    private EmailService emailService;

    public void saveNewUser(User newUser) {
        String otp = generateOTP.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // OTP valid for 10 minutes

        newUser.setOtp(otp);
        newUser.setOtpExpirationTime(expirationTime);
        newUser.setRoles(List.of("USER"));

        emailService.verifyEmail(newUser.getEmail(), "Your one time password is: "+otp);
        userRepository.save(newUser);
    }


    public boolean verifyOtp(String email, String otp) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.getOtp().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpirationTime())) {
            user.setOtp(null);
            user.setOtpExpirationTime(null);
            userRepository.save(user);
            return true;
        } else {
            return false;

        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
