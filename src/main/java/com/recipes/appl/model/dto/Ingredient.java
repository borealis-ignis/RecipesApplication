package com.recipes.appl.model.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class Ingredient {
	private Long id;
	private String name;
	private Double count;
	private String measure;
	private List<Component> components;
}
