package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import com.skillifyme.auth.Skillify.Me.Auth.model.TemporaryUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.InstructorRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.TemporaryUserRepository;
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
    private TemporaryUserRepository temporaryUserRepository;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GenerateOTP generateOTP;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InstructorRepository instructorRepository;

    public void resetPassword(String email, String newPassword, String userType) {
        AuthUser existingUser = registerService.findUserByEmailAndType(email, userType);
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        if (userType.equalsIgnoreCase("USER")) {
            userRepository.save((User) existingUser);
        } else {
            instructorRepository.save((Instructor) existingUser);
        }
    }

    public void sendOtpForVerification(String email, String userType) {
        AuthUser existingUser = registerService.findUserByEmailAndType(email, userType);
        if (existingUser != null) {
            String otp = generateOTP.generateOtp();
            String subject = "Email confirmation";
            emailService.sendEmail(email, subject, "Your verification code is " + otp + " valid for 10 minutes");
            TemporaryUser temporaryUser = new TemporaryUser(email, "", "");
            temporaryUser.setUserType(userType);
            temporaryUser.setOtp(otp);
            temporaryUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        }
    }

    public boolean verifyOtp(String email, String otp, String userType) throws UsernameNotFoundException {
        TemporaryUser tempUser = temporaryUserRepository.findByEmail(email);
        if (tempUser != null && tempUser.getUserType().equalsIgnoreCase(userType)) {
            if (tempUser.getOtp().equals(otp) && LocalDateTime.now().isBefore(tempUser.getOtpExpirationTime())) {
//                tempUser.setOtp(null);
//                tempUser.setOtpExpirationTime(null);
//                tempUser.setVerified(true);
//                temporaryUserRepository.save(tempUser);
                temporaryUserRepository.delete(tempUser);
                return true;
            }
        }
        return false;
    }
}
