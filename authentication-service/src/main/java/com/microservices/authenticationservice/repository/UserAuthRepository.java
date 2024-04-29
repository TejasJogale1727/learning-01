package com.microservices.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.authenticationservice.entities.UserAuth;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, String>{

}
