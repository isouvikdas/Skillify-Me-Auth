package com.skillifyme.auth.Skillify.Me.Auth.repository;

import com.skillifyme.auth.Skillify.Me.Auth.model.TemporaryUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryUserRepository extends MongoRepository<TemporaryUser, ObjectId> {

    TemporaryUser findByEmail(String email);
}
