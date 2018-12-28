package com.recipes.appl.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.CollectionUtils;

import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;

/**
 * @author Kastalski Sergey
 */
public interface RecipesDAO extends JpaRepository<RecipeDbo, Long>, JpaSpecificationExecutor<RecipeDbo> {
	
	@Query("select r from RecipeDbo r inner join r.dishType d where r.name = :recipeName and d.id = :identificator")
	List<RecipeDbo> findAllByNameAndDishType(@Param("recipeName") String name, @Param("identificator") Long id);
	
	@Query("select distinct r from RecipeDbo r inner join r.recipeIngredients ri where ri.ingredient.id = :ingredientId")
	List<RecipeDbo> findAllByIngredientId(@Param("ingredientId") Long ingredientId);
	
	default List<RecipeDbo> findAllByFilterParams(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList) {
		final Specification<RecipeDbo> filteringSpecification = new Specification<RecipeDbo>() {
			
			private static final long serialVersionUID = 9199576284337624784L;
			
			@Override
			public Predicate toPredicate(final Root<RecipeDbo> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
				final Predicate predicate = cb.conjunction();
				if (namePart != null) {
		            predicate.getExpressions().add(cb.like(root.get("name"), "%" + namePart + "%"));
		        }
				if (dishTypeId != null) {
		            predicate.getExpressions().add(cb.equal(root.get("dishType").get("id"), dishTypeId));
		        }
				
				final boolean withIngredients = !CollectionUtils.isEmpty(ingredientIdList);
				final boolean withComponents = !CollectionUtils.isEmpty(componentIdList);
				if (withIngredients || withComponents) {
					query.distinct(true);
					final Join<RecipeDbo, RecipeIngredientDbo> ingredientJoin = root.join("recipeIngredients", JoinType.INNER).join("ingredient", JoinType.INNER);
					if (withIngredients) {
						predicate.getExpressions().add(ingredientJoin.get("id").in(ingredientIdList));
					}
					if (withComponents) {
						predicate.getExpressions().add(ingredientJoin.join("components", JoinType.INNER).get("id").in(componentIdList));
					}
				}
				return predicate;
			}
		};
		
		return findAll(filteringSpecification);
	}
	
	@Query("select distinct r from RecipeDbo r inner join r.dishType d where d.id = :dishTypeId")
	List<RecipeDbo> findAllByDishTypeId(@Param("dishTypeId") Long id);
	
}
