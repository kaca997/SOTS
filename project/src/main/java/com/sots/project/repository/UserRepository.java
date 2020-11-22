package com.sots.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.sots.project.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	
}
