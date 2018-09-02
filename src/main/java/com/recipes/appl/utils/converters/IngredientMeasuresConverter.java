package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.IngredientMeasure;
import com.recipes.appl.model.dto.IngredientMeasureDto;

/**
 * @author Kastalski Sergey
 */
public class IngredientMeasuresConverter {
	
	public static IngredientMeasureDto convertDboToDto(final IngredientMeasure dbo) {
		final IngredientMeasureDto dto = new IngredientMeasureDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}
	
	public static IngredientMeasure convertDtoToDbo(final IngredientMeasureDto dto) {
		final IngredientMeasure dbo = new IngredientMeasure();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}
	
	public static List<IngredientMeasureDto> convertDboToDto(final List<IngredientMeasure> dboList) {
		final List<IngredientMeasureDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<IngredientMeasure> convertDtoToDbo(final List<IngredientMeasureDto> dtoList) {
		final List<IngredientMeasure> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
