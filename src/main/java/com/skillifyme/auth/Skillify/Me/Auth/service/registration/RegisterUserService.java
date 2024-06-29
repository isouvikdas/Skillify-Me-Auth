package com.skillifyme.auth.Skillify.Me.Auth.service.registration;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import com.skillifyme.auth.Skillify.Me.Auth.repository.UserRepository;
import com.skillifyme.auth.Skillify.Me.Auth.service.EmailService;
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
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenerateOTP generateOTP;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean checkEmailVerification(String email) {
        User user = userRepository.findByEmail(email);
        return user.isVerified();
    }

    public void saveNewUser(String email, String userName, String password) {
        User newUser = userRepository.findByEmail(email);
        newUser.setUserName(userName);
        newUser.setEmail(newUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setDateAndTime(LocalDateTime.now());
        newUser.setRoles(List.of("USER"));
        newUser.setOtp(null);
        newUser.setOtpExpirationTime(null);
        newUser.setVerified(newUser.isVerified());
        userRepository.save(newUser);
    }


    public boolean verifyOtp(String email, String otp) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.getOtp().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpirationTime())) {
            user.setUserName(user.getUserName());
            user.setEmail(user.getEmail());
            user.setPassword(user.getPassword());
            user.setDateAndTime(user.getDateAndTime());
            user.setRoles(user.getRoles());
            user.setOtp(null);
            user.setOtpExpirationTime(null);
            user.setVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void sendOtpForVerification(String email) {
        String otp = generateOTP.generateOtp();
        String subject = "Email confirmation";
        emailService.sendEmail(email, subject, "Your verification code is "+ otp +" Valid for 10 minutes");
        User newUser = new User(email, "", "");
        newUser.setRoles(List.of(""));
        newUser.setOtp(otp);
        newUser.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        newUser.setDateAndTime(LocalDateTime.now());
        userRepository.save(newUser);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
