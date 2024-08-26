package com.org.Shopping_App.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Products;

@Repository
public interface ProductRepo extends JpaRepository<Products, Integer>{

}
