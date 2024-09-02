package com.org.Shopping_App.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByEmail(String email);
}
