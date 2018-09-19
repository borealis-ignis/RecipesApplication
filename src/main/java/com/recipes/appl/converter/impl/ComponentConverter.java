package com.recipes.appl.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.recipes.appl.converter.DtoDboConverter;
import com.recipes.appl.model.dbo.ComponentDbo;
import com.recipes.appl.model.dto.ComponentDto;

/**
 * @author Kastalski Sergey
 */
@Service
public class ComponentConverter implements DtoDboConverter<ComponentDto, ComponentDbo> {

	@Override
	public ComponentDto convertDboToDto(final ComponentDbo dbo) {
		final ComponentDto dto = new ComponentDto();
		dto.setId(dbo.getId());
		dto.setName(dbo.getName());
		return dto;
	}

	@Override
	public ComponentDbo convertDtoToDbo(final ComponentDto dto) {
		final ComponentDbo dbo = new ComponentDbo();
		dbo.setId(dto.getId());
		dbo.setName(dto.getName());
		return dbo;
	}

	@Override
	public List<ComponentDto> convertDboToDto(final List<ComponentDbo> dboList) {
		final List<ComponentDto> result = new ArrayList<>();
		dboList.forEach((dbo) -> { result.add(convertDboToDto(dbo)); });
		return result;
	}

	@Override
	public List<ComponentDbo> convertDtoToDbo(final List<ComponentDto> dtoList) {
		final List<ComponentDbo> result = new ArrayList<>();
		dtoList.forEach((dto) -> { result.add(convertDtoToDbo(dto)); });
		return result;
	}
	
}
