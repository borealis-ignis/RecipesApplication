package com.recipes.appl.model.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class RecipeIngredient {
	private Long id;
	private Ingredient ingredient;
	private Double count;
	private IngredientMeasure ingredientMeasure;
}
