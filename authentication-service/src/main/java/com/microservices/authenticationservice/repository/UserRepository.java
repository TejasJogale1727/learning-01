package com.microservices.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.authenticationservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	public Optional<User> findByEmail(String email);
}
