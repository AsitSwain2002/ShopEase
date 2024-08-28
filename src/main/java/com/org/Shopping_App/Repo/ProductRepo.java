package com.org.Shopping_App.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Products;

@Repository
public interface ProductRepo extends JpaRepository<Products, Integer> {

	@Query("SELECT p FROM Products p WHERE p.catagory = :catagory")
	List<Products> findAllByCatagory(@Param("catagory") String catagory);
}
