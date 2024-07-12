package com.skillifyme.auth.Skillify.Me.Auth.service;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import com.skillifyme.auth.Skillify.Me.Auth.model.TemporaryUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.InstructorRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.TemporaryUserRepository;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import com.skillifyme.auth.Skillify.Me.Auth.utils.GenerateOTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private GenerateOTP generateOTP;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TemporaryUserRepository temporaryUserRepository;


    public boolean checkEmailVerification(String email, String userType) {
        AuthUser authUser = findUserByEmailAndType(email, userType);
        if (authUser != null) {
            return authUser.isVerified();
        } else {
            TemporaryUser tempUser = temporaryUserRepository.findByEmailAndUserType(email, userType.toUpperCase());
            return tempUser != null && tempUser.isVerified();
        }
    }

    public boolean checkEmailVerificationForLogin(String email, String userType) {
        AuthUser authUser = findUserByEmailAndType(email, userType);
        return authUser != null && authUser.isVerified();
    }

    public void saveNewUser(String email, String userName, String password, String userType) {
        AuthUser newUser = findUserByEmailAndType(email, userType);
        if (newUser == null) {
            TemporaryUser tempUser = temporaryUserRepository.findByEmail(email);
            if (tempUser != null && tempUser.getUserType().equalsIgnoreCase(userType)) {
                newUser = userType.equalsIgnoreCase("USER") ?
                        new User(email, userName, passwordEncoder.encode(password)) :
                        new Instructor(email, userName, passwordEncoder.encode(password));
                newUser.setDateAndTime(LocalDateTime.now());
                newUser.setRoles(List.of(userType.toUpperCase()));
                newUser.setVerified(tempUser.isVerified());
                newUser.setUserType(tempUser.getUserType());
                if (newUser instanceof User) {
                    userRepository.save((User) newUser);
                } else {
                    instructorRepository.save((Instructor) newUser);
                }
                temporaryUserRepository.delete(tempUser);
            }
        }
    }

    public boolean verifyOtp(String email, String otp, String userType) {
        TemporaryUser tempUser = temporaryUserRepository.findByEmail(email);
        if (tempUser != null && tempUser.getUserType().equalsIgnoreCase(userType)) {
            if (tempUser.getOtp().equals(otp) && LocalDateTime.now().isBefore(tempUser.getOtpExpirationTime())) {
                tempUser.setOtp(null);
                tempUser.setOtpExpirationTime(null);
                tempUser.setVerified(true);
                temporaryUserRepository.save(tempUser);
                return true;
            }
        }
        return false;
    }

    public void sendOtpForVerification(String email, String userType) {
        AuthUser existingUser = findUserByEmailAndType(email, userType);
        if (existingUser == null) {
            String otp = generateOTP.generateOtp();
            String subject = "Email confirmation";
            emailService.sendEmail(email, subject, "Your verification code is " + otp + " valid for 10 minutes");
            TemporaryUser tempUser = new TemporaryUser(email, "", "");
            tempUser.setUserType(userType.toUpperCase());
            tempUser.setRoles(List.of(userType.toUpperCase()));
            tempUser.setOtp(otp);
            tempUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
            tempUser.setDateAndTime(LocalDateTime.now());
            temporaryUserRepository.save(tempUser);
        }
    }

    public AuthUser findUserByEmailAndType(String email, String userType) {
        if (userType.equalsIgnoreCase("USER")) {
            return userRepository.findByEmail(email);
        } else if (userType.equalsIgnoreCase("INSTRUCTOR")) {
            return instructorRepository.findByEmail(email);
        }
        return null;
    }

}
