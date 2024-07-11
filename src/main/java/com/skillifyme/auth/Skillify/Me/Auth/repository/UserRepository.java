package com.skillifyme.auth.Skillify.Me.Auth.repository;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import com.skillifyme.auth.Skillify.Me.Auth.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends AuthUserRepository<User>{

}



