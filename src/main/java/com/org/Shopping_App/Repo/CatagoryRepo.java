package com.org.Shopping_App.Repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Catagory;

@Repository
public interface CatagoryRepo extends JpaRepository<Catagory, Integer> {

	boolean existsByName(String name);

	Page<Catagory> findByStatusTrue(Pageable page);
}