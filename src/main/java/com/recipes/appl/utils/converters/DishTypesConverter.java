package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.DishType;
import com.recipes.appl.model.dto.DishTypeDto;

/**
 * @author Kastalski Sergey
 */
public class DishTypesConverter {
	
	public static DishTypeDto convertDboToDto(final DishType dbo) {
		final DishTypeDto dto = new DishTypeDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}
	
	public static DishType convertDtoToDbo(final DishTypeDto dto) {
		final DishType dbo = new DishType();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}
	
	public static List<DishTypeDto> convertDboToDto(final List<DishType> dboList) {
		final List<DishTypeDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<DishType> convertDtoToDbo(final List<DishTypeDto> dtoList) {
		final List<DishType> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
