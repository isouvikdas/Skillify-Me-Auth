package com.skillifyme.auth.Skillify.Me.Auth.utils;

import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class GenerateOTP {
    public String generateOtp() {
        Random random = new Random();
        return String.valueOf(random.nextInt(1000000));
    }

}
