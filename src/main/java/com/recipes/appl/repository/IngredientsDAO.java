package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipes.appl.model.dbo.IngredientDbo;

/**
 * @author Kastalski Sergey
 */
public interface IngredientsDAO extends JpaRepository<IngredientDbo, Long> {
	
	@Query("select i from IngredientDbo i order by i.name asc")
	List<IngredientDbo> selectIngredients();
	
	List<IngredientDbo> findAllByName(String name);
	
}
