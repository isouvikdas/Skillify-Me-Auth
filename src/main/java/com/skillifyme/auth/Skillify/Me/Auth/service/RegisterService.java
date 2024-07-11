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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    public boolean checkEmailVerification(String email) {
        AuthUser authUser = userRepository.findByEmail(email);
        if (authUser == null) {
            authUser = instructorRepository.findByEmail(email);
        }
        return authUser.isVerified();
    }

    public void saveNewUser(String email,
                            String userName,
                            String password,
                            Class<? extends AuthUser> userType) {
        AuthUser newUser;
        if (userType.equals(User.class)) {
            newUser = userRepository.findByEmail(email);
            newUser.setRoles(List.of("USER"));
        } else {
            newUser = instructorRepository.findByEmail(email);
            newUser.setRoles(List.of("INSTRUCTOR"));
        }
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setDateAndTime(LocalDateTime.now());
        if (userType.equals(User.class)) {
            userRepository.save((User) newUser);
        } else {
            instructorRepository.save((Instructor) newUser);
        }
    }


    public boolean verifyOtp(String email, String otp) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByEmail(email);
        if (authUser == null) {
            authUser = instructorRepository.findByEmail(email);
        }
        if (authUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (authUser.getOtp().equals(otp) && LocalDateTime.now().isBefore(authUser.getOtpExpirationTime())) {
            authUser.setOtp(null);
            authUser.setOtpExpirationTime(null);
            authUser.setVerified(true);
            if (authUser instanceof User) {
                userRepository.save((User) authUser);
            } else {
                instructorRepository.save((Instructor) authUser);
            }
            return true;
        } else {
            return false;
        }
    }

    public void sendOtpForVerification(String email, String userType) {
        String otp = generateOTP.generateOtp();
        String subject = "Email confirmation";
        emailService.sendEmail(email, subject, "Your verification code is "+ otp +" valid for 10 minutes");
        TemporaryUser tempUser = temporaryUserRepository.findByEmail(email);
        if (tempUser == null) {
            tempUser = new TemporaryUser("", "", "");
            tempUser.setEmail(email);
            tempUser.setRoles(List.of(userType.toUpperCase()));
            tempUser.setUserType(userType.toUpperCase());
        }
        tempUser.setOtp(otp);
        tempUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        tempUser.setDateAndTime(LocalDateTime.now());
        temporaryUserRepository.save(tempUser);
    }
}
