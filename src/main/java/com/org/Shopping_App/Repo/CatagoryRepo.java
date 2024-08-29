package com.org.Shopping_App.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Catagory;

@Repository
public interface CatagoryRepo extends JpaRepository<Catagory, Integer> {

	 Boolean existsByName(String name);
	 
	 List<Catagory> findByStatusTrue();
}