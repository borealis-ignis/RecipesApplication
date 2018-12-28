package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipes.appl.model.dbo.RecipeIngredientDbo;

/**
 * @author Kastalski Sergey
 */
public interface RecipeIngredientDAO extends JpaRepository<RecipeIngredientDbo, Long> {
	
	@Query("select distinct i from RecipeIngredientDbo i inner join i.ingredientMeasure im where im.id = :measureId")
	List<RecipeIngredientDbo> findAllByMeasureId(@Param("measureId") Long id);
	
}
