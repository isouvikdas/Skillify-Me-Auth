package com.skillifyme.auth.Skillify.Me.Auth.model;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "Users")
public class User implements AuthUser{
    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String email;
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private LocalDateTime dateAndTime;
    private List<String> roles;
    private String otp;
    private LocalDateTime otpExpirationTime;
    private boolean isVerified = false;
    private String userType;
}
