package com.skillifyme.auth.Skillify.Me.Auth.repository;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthUserRepository<T extends AuthUser> extends MongoRepository<T, ObjectId> {
    T findByUserName(String username);
    T findByEmail(String email);
}
