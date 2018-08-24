package com.recipes.appl.model.dto;

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
	private String dishType;
	private String name;
	private List<Ingredient> ingredients;
	private String description;
}
