package com.skillifyme.auth.Skillify.Me.Auth.repository;

import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {


    User findByUserName(String email);

    User findByEmail(String email);
}
