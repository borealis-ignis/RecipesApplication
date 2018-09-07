package com.recipes.appl.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipes.appl.model.dbo.Ingredient;

/**
 * @author Kastalski Sergey
 */
public interface IngredientsDAO extends JpaRepository<Ingredient, Long> {
	
	@Query("select i from Ingredient i order by i.name asc")
	public List<Ingredient> selectIngredients();
	
	public List<Ingredient> findAllByName(String name);
	
}