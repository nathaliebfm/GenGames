package com.gengames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gengames.model.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository <Categories, Long>{
	
	public List<Categories> findAllByTypeContainingIgnoreCase(String type);

}
