package com.recipes.appl.utils.converters;

import java.util.ArrayList;
import java.util.List;

import com.recipes.appl.model.dbo.ComponentDbo;
import com.recipes.appl.model.dto.ComponentDto;

/**
 * @author Kastalski Sergey
 */
public class ComponentsConverter {
	
	public static ComponentDto convertDboToDto(final ComponentDbo dbo) {
		final ComponentDto dto = new ComponentDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}
	
	public static ComponentDbo convertDtoToDbo(final ComponentDto dto) {
		final ComponentDbo dbo = new ComponentDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}
	
	public static List<ComponentDto> convertDboToDto(final List<ComponentDbo> dboList) {
		final List<ComponentDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}
	
	public static List<ComponentDbo> convertDtoToDbo(final List<ComponentDto> dtoList) {
		final List<ComponentDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
}
