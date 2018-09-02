package com.recipes.appl.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class IngredientDto {
	private Long id;
	private Long ingredientNameId;
	private String name;
	private Double count;
	private IngredientMeasureDto measure;
	private List<ComponentDto> components;
}
