package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class RecipeIngredientConverter implements DtoDboConverter<IngredientDto, RecipeIngredientDbo> {
	
	private IngredientMeasureConverter ingredientMeasureConverter;
	
	@Autowired
	public RecipeIngredientConverter(final IngredientMeasureConverter ingredientMeasureConverter) {
		this.ingredientMeasureConverter = ingredientMeasureConverter;
	}
	
	@Override
	public IngredientDto convertDboToDto(final RecipeIngredientDbo dbo) {
		final IngredientDto dto = new IngredientDto();
		dto.setId(dbo.getId());
		dto.setCount(dbo.getCount());
		dto.setMeasure(ingredientMeasureConverter.convertDboToDto(dbo.getIngredientMeasure()));
		dto.setName(dbo.getIngredient().getName());
		
		final IngredientDbo ingredient = dbo.getIngredient();
		dto.setName(ingredient.getName());
		dto.setIngredientNameId(ingredient.getId());
		
		return dto;
	}

	@Override
	public RecipeIngredientDbo convertDtoToDbo(final IngredientDto dto) {
		final RecipeIngredientDbo dbo = new RecipeIngredientDbo();
		dbo.setId(dto.getId());
		dbo.setCount(dto.getCount());
		dbo.setIngredientMeasure(ingredientMeasureConverter.convertDtoToDbo(dto.getMeasure()));
		
		final IngredientDbo ingredient = new IngredientDbo();
		ingredient.setId(dto.getIngredientNameId());
		ingredient.setName(dto.getName());
		dbo.setIngredient(ingredient);
		
		return dbo;
	}

	@Override
	public List<IngredientDto> convertDboToDto(final List<RecipeIngredientDbo> dboList) {
		final List<IngredientDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<RecipeIngredientDbo> convertDtoToDbo(final List<IngredientDto> dtoList) {
		final List<RecipeIngredientDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
	
}
