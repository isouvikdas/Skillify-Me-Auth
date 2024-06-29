package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import com.skillifyme.auth.Skillify.Me.Auth.utils.GenerateOTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private GenerateOTP generateOTP;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String email, String newPassword) {
        User currentUser = userRepository.findByEmail(email);
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    public boolean checkEmailVerification(String email) {
        User user = userRepository.findByEmail(email);
        return user.isVerified();
    }

    public void sendOtpForVerification(String email) {
        String otp = generateOTP.generateOtp();
        String subject = "Email confirmation";
        emailService.sendEmail(email, subject, "Your verification code is " + otp + " Valid for 10 minutes");
        User currentUser = userRepository.findByEmail(email);
        currentUser.setOtp(otp);
        currentUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        userRepository.save(currentUser);
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
}
