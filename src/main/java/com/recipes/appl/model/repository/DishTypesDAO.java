package com.recipes.appl.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipes.appl.model.dbo.DishType;

/**
 * @author Kastalski Sergey
 */
public interface DishTypesDAO extends JpaRepository<DishType, Long> {
	
	public List<DishType> findAllByOrderByIdAsc();
	
}
