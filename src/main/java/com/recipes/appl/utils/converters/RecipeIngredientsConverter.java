package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
public class RecipeIngredientsConverter {
	
	public static IngredientDto convertDboToDto(final RecipeIngredientDbo dbo) {
		final IngredientDto dto = new IngredientDto();
		dto.setId(dbo.getId());
		dto.setCount(dbo.getCount());
		dto.setMeasure(IngredientMeasuresConverter.convertDboToDto(dbo.getIngredientMeasure()));
		dto.setName(dbo.getIngredient().getName());
		
		final IngredientDbo ingredient = dbo.getIngredient();
		dto.setName(ingredient.getName());
		dto.setIngredientNameId(ingredient.getId());
		
		return dto;
	}
	
	public static RecipeIngredientDbo convertDtoToDbo(final IngredientDto dto) {
		final RecipeIngredientDbo dbo = new RecipeIngredientDbo();
		dbo.setId(dto.getId());
		dbo.setCount(dto.getCount());
		dbo.setIngredientMeasure(IngredientMeasuresConverter.convertDtoToDbo(dto.getMeasure()));
		
		final IngredientDbo ingredient = new IngredientDbo();
		ingredient.setId(dto.getIngredientNameId());
		ingredient.setName(dto.getName());
		dbo.setIngredient(ingredient);
		
		return dbo;
	}
	
	public static List<IngredientDto> convertDboToDto(final List<RecipeIngredientDbo> dboList) {
		final List<IngredientDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<RecipeIngredientDbo> convertDtoToDbo(final List<IngredientDto> dtoList) {
		final List<RecipeIngredientDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
