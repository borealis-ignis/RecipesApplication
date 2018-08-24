package com.recipes.appl.model.dbo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
public class Component {
	private Long id;
	private String name;
	private List<Ingredient> ingredients;
}
