package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.DishTypeDbo;
import com.recipes.appl.model.dto.DishTypeDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class DishTypeConverter implements DtoDboConverter<DishTypeDto, DishTypeDbo> {

	@Override
	public DishTypeDto convertDboToDto(final DishTypeDbo dbo) {
		final DishTypeDto dto = new DishTypeDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}

	@Override
	public DishTypeDbo convertDtoToDbo(final DishTypeDto dto) {
		final DishTypeDbo dbo = new DishTypeDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}

	@Override
	public List<DishTypeDto> convertDboToDto(final List<DishTypeDbo> dboList) {
		final List<DishTypeDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<DishTypeDbo> convertDtoToDbo(final List<DishTypeDto> dtoList) {
		final List<DishTypeDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}

}
