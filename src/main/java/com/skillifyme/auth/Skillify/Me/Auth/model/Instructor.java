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
@Document(collection = "Instructors")
public class Instructor implements AuthUser{
    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private LocalDateTime dateAndTime;
    private List<String> roles;
    private String otp;
    private LocalDateTime otpExpirationTime;
    private boolean isVerified = false;
}
