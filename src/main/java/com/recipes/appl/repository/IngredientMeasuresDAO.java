package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recipes.appl.model.dbo.IngredientMeasure;

/**
 * @author Kastalski Sergey
 */
public interface IngredientMeasuresDAO extends JpaRepository<IngredientMeasure, Long> {
	
	List<IngredientMeasure> findAllByOrderByIdAsc();
	
}
