package com.skillifyme.auth.Skillify.Me.Auth.model;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthUser {

    String getEmail();
    String getUsername();
    String getPassword();
    LocalDateTime getDateAndTime();
    List<String> getRoles();
    String getOtp();
    LocalDateTime getOtpExpirationTime();
    boolean isVerified();

    void setEmail(String email);
    void setUsername(String username);
    void setPassword(String password);
    void setDateAndTime(LocalDateTime dateAndTime);
    void setRoles(List<String> roles);
    void setOtp(String otp);
    void setOtpExpirationTime(LocalDateTime otpExpirationTime);
    void setVerified(boolean verified);
}
