package com.recipes.appl.model.dbo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class Recipe {
	private Long id;
	private DishType dishType;
	private String name;
	private List<Ingredient> ingredients;
	private String description;
}
