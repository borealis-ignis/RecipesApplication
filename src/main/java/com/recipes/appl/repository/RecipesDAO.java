package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipes.appl.model.dbo.RecipeDbo;

/**
 * @author Kastalski Sergey
 */
public interface RecipesDAO extends JpaRepository<RecipeDbo, Long> {
	
	@Query("select r from RecipeDbo r inner join r.dishType d where r.name = :recipeName and d.id = :identificator")
	List<RecipeDbo> findAllByNameAndDishType(@Param("recipeName") String name, @Param("identificator") Long id);
	
}
