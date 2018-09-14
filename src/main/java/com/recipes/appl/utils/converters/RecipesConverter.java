package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.RecipeDto;

/**
 * @author Kastalski Sergey
 */
public class RecipesConverter {
	
	public static RecipeDto convertDboToDto(final RecipeDbo dbo) {
		final RecipeDto dto = new RecipeDto();
		dto.setId(dbo.getId());
		dto.setDishType(DishTypesConverter.convertDboToDto(dbo.getDishType()));
		dto.setName(dbo.getName());
		dto.setIngredients(RecipeIngredientsConverter.convertDboToDto(dbo.getRecipeIngredients()));
		dto.setDescription(dbo.getDescription());
		return dto;
	}
	
	public static RecipeDbo convertDtoToDbo(final RecipeDto dto) {
		final RecipeDbo dbo = new RecipeDbo();
		dbo.setId(dto.getId());
		dbo.setDishType(DishTypesConverter.convertDtoToDbo(dto.getDishType()));
		dbo.setName(dto.getName());
		dbo.setDescription(dto.getDescription());
		
		final List<RecipeIngredientDbo> recipeIngredients = RecipeIngredientsConverter.convertDtoToDbo(dto.getIngredients());
		recipeIngredients.forEach((recipeIngredient) -> { recipeIngredient.setRecipe(dbo); });
		dbo.setRecipeIngredients(recipeIngredients);
		
		return dbo;
	}
	
	public static List<RecipeDto> convertDboToDto(final List<RecipeDbo> dboList) {
		final List<RecipeDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<RecipeDbo> convertDtoToDbo(final List<RecipeDto> dtoList) {
		final List<RecipeDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
