package com.org.Shopping_App.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	Page<User> findByRole(String role, Pageable page);

	User findByToken(String token);

	@Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<User> findAllByName(@Param("name") String name);

	boolean existsByName(String name);
	boolean existsByEmail(String email);
	boolean existsByMobile(String mobile);

}
