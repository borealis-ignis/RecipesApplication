package com.recipes.appl.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class RecipeDto {
	private Long id;
	private DishTypeDto dishType;
	private String name;
	private String image;
	private List<IngredientDto> ingredients;
	private String description;
}
