package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
public class IngredientsConverter {
	
	public static IngredientDto convertDboToDto(final IngredientDbo dbo) {
		final IngredientDto dto = new IngredientDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		dto.setComponents(ComponentsConverter.convertDboToDto(dbo.getComponents()));
		return dto;
	}
	
	public static IngredientDbo convertDtoToDbo(final IngredientDto dto) {
		final IngredientDbo dbo = new IngredientDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		dbo.setComponents(ComponentsConverter.convertDtoToDbo(dto.getComponents()));
		return dbo;
	}
	
	public static List<IngredientDto> convertDboToDto(final List<IngredientDbo> dboList) {
		final List<IngredientDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<IngredientDbo> convertDtoToDbo(final List<IngredientDto> dtoList) {
		final List<IngredientDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
