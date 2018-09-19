package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class IngredientConverter implements DtoDboConverter<IngredientDto, IngredientDbo> {
	
	private ComponentConverter componentConverter;
	
	@Autowired
	public IngredientConverter(final ComponentConverter componentConverter) {
		this.componentConverter = componentConverter;
	}
	
	@Override
	public IngredientDto convertDboToDto(final IngredientDbo dbo) {
		final IngredientDto dto = new IngredientDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		dto.setComponents(componentConverter.convertDboToDto(dbo.getComponents()));
		return dto;
	}

	@Override
	public IngredientDbo convertDtoToDbo(final IngredientDto dto) {
		final IngredientDbo dbo = new IngredientDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		dbo.setComponents(componentConverter.convertDtoToDbo(dto.getComponents()));
		return dbo;
	}

	@Override
	public List<IngredientDto> convertDboToDto(final List<IngredientDbo> dboList) {
		final List<IngredientDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<IngredientDbo> convertDtoToDbo(final List<IngredientDto> dtoList) {
		final List<IngredientDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}

}
