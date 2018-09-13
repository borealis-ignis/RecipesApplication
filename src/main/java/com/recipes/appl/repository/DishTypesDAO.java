package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipes.appl.model.dbo.DishType;

/**
 * @author Kastalski Sergey
 */
public interface DishTypesDAO extends JpaRepository<DishType, Long> {
	
	List<DishType> findAllByOrderByIdAsc();
	
}
