package com.skillifyme.auth.Skillify.Me.Auth.repository;

import com.skillifyme.auth.Skillify.Me.Auth.model.AuthUser;
import com.skillifyme.auth.Skillify.Me.Auth.model.Instructor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends AuthUserRepository<Instructor> {
}
