package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.RecipeDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class RecipeConverter implements DtoDboConverter<RecipeDto, RecipeDbo> {
	
	private DishTypeConverter dishTypeConverter;
	
	private RecipeIngredientConverter recipeIngredientConverter;
	
	@Autowired
	public RecipeConverter(final DishTypeConverter dishTypeConverter, final RecipeIngredientConverter recipeIngredientConverter) {
		this.dishTypeConverter = dishTypeConverter;
		this.recipeIngredientConverter = recipeIngredientConverter;
	}
	
	@Override
	public RecipeDto convertDboToDto(final RecipeDbo dbo) {
		final RecipeDto dto = new RecipeDto();
		dto.setId(dbo.getId());
		dto.setDishType(dishTypeConverter.convertDboToDto(dbo.getDishType()));
		dto.setName(dbo.getName());
		dto.setIngredients(recipeIngredientConverter.convertDboToDto(dbo.getRecipeIngredients()));
		dto.setDescription(dbo.getDescription());
		return dto;
	}

	@Override
	public RecipeDbo convertDtoToDbo(final RecipeDto dto) {
		final RecipeDbo dbo = new RecipeDbo();
		dbo.setId(dto.getId());
		dbo.setDishType(dishTypeConverter.convertDtoToDbo(dto.getDishType()));
		dbo.setName(dto.getName());
		dbo.setDescription(dto.getDescription());
		
		final List<RecipeIngredientDbo> recipeIngredients = recipeIngredientConverter.convertDtoToDbo(dto.getIngredients());
		recipeIngredients.forEach((recipeIngredient) -> { recipeIngredient.setRecipe(dbo); });
		dbo.setRecipeIngredients(recipeIngredients);
		
		return dbo;
	}

	@Override
	public List<RecipeDto> convertDboToDto(final List<RecipeDbo> dboList) {
		final List<RecipeDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<RecipeDbo> convertDtoToDbo(final List<RecipeDto> dtoList) {
		final List<RecipeDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
	
}
