package com.recipes.appl.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;

import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.repository.specifications.RecipesSpecification;

/**
 * @author Kastalski Sergey
 */
public interface RecipesDAO extends JpaRepository<RecipeDbo, Long>, JpaSpecificationExecutor<RecipeDbo> {
	
	@Query("select r from RecipeDbo r inner join r.dishType d where r.name = :recipeName and d.id = :identificator")
	List<RecipeDbo> findAllByNameAndDishType(@Param("recipeName") String name, @Param("identificator") Long id);
	
	@Query("select distinct r from RecipeDbo r inner join r.recipeIngredients ri where ri.ingredient.id = :ingredientId")
	List<RecipeDbo> findAllByIngredientId(@Param("ingredientId") Long ingredientId);
	
	default List<RecipeDbo> findAllByFilterParams(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList, final Pageable pageable) {
		final Specification<RecipeDbo> filteringSpecification = new RecipesSpecification(namePart, dishTypeId, ingredientIdList, componentIdList);
		final Page<RecipeDbo> page = findAll(filteringSpecification, pageable);
		return page.getContent();
	}
	
	default long recipesCount(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList) {
		final Specification<RecipeDbo> filteringSpecification = new RecipesSpecification(namePart, dishTypeId, ingredientIdList, componentIdList);
		return count(filteringSpecification);
	}
	
	@Query("select distinct r from RecipeDbo r inner join r.dishType d where d.id = :dishTypeId")
	List<RecipeDbo> findAllByDishTypeId(@Param("dishTypeId") Long id);
	
}
