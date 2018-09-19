package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.IngredientMeasureDbo;
import com.recipes.appl.model.dto.IngredientMeasureDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class IngredientMeasureConverter implements DtoDboConverter<IngredientMeasureDto, IngredientMeasureDbo> {

	@Override
	public IngredientMeasureDto convertDboToDto(final IngredientMeasureDbo dbo) {
		final IngredientMeasureDto dto = new IngredientMeasureDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}

	@Override
	public IngredientMeasureDbo convertDtoToDbo(final IngredientMeasureDto dto) {
		final IngredientMeasureDbo dbo = new IngredientMeasureDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}

	@Override
	public List<IngredientMeasureDto> convertDboToDto(final List<IngredientMeasureDbo> dboList) {
		final List<IngredientMeasureDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<IngredientMeasureDbo> convertDtoToDbo(final List<IngredientMeasureDto> dtoList) {
		final List<IngredientMeasureDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
	
}
