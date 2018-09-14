package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recipes.appl.model.dbo.IngredientMeasureDbo;

/**
 * @author Kastalski Sergey
 */
public interface IngredientMeasuresDAO extends JpaRepository<IngredientMeasureDbo, Long> {
	
	List<IngredientMeasureDbo> findAllByOrderByIdAsc();
	
}
