package com.example.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long>{
	Optional<AppUser> findByUsername(String username);
	List<AppUser> findByUsernameContainingIgnoreCase(String keyword);
}
