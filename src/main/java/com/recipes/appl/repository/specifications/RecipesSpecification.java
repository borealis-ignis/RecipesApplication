package com.recipes.appl.repository.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;

/**
 * @author Kastalski Sergey
 */
public class RecipesSpecification implements Specification<RecipeDbo> {

	private static final long serialVersionUID = 1813498365870259097L;
	
	private String namePart;
	private Long dishTypeId;
	private List<Long> ingredientIdList;
	private List<Long> componentIdList;
	
	public RecipesSpecification(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList) {
		this.namePart = namePart;
		this.dishTypeId = dishTypeId;
		this.ingredientIdList = ingredientIdList;
		this.componentIdList = componentIdList;
	}
	
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
	
}
