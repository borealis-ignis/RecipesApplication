package com.recipes.appl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.recipes.appl.model.dbo.Component;
import com.recipes.appl.model.dbo.Ingredient;
import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
public class MockData {
	
	// dto data
	public static List<IngredientDto> listDtoIngredients(boolean withComponents) {
		final List<IngredientDto> ingredients = new ArrayList<>();
		ingredients.add(dtoAlmondIngredient(withComponents, true));
		ingredients.add(dtoCornIngredient(withComponents, true));
		return ingredients;
	}
	
	public static IngredientDto dtoAlmondIngredient(boolean withComponents, boolean withId) {
		final IngredientDto ingredient = new IngredientDto();
		if (withId) {
			ingredient.setId(1L);
		}
		ingredient.setName("Миндаль");
		if (withComponents) {
			ingredient.setComponents(listDtoComponents());
		}
		return ingredient;
	}
	
	public static IngredientDto dtoCornIngredient(boolean withComponents, boolean withId) {
		final IngredientDto ingredient = new IngredientDto();
		if (withId) {
			ingredient.setId(2L);
		}
		ingredient.setName("Кукуруза");
		if (withComponents) {
			ingredient.setComponents(listDtoComponents());
		}
		return ingredient;
	}
	
	
	public static List<ComponentDto> listDtoComponents() {
		final List<ComponentDto> components = new ArrayList<>();
		components.add(dtoCalciumComponent());
		components.add(dtoMagniumComponent());
		return components;
	}
	
	public static ComponentDto dtoCalciumComponent() {
		final ComponentDto component = new ComponentDto();
		component.setId(1L);
		component.setName("Кальций");
		return component;
	}
	
	public static ComponentDto dtoMagniumComponent() {
		final ComponentDto component = new ComponentDto();
		component.setId(2L);
		component.setName("Магний");
		return component;
	}
	
	// dbo data
	public static Optional<Ingredient> optionalDboIngredientWithComponents() {
		final Ingredient ingredient = new Ingredient();
		ingredient.setId(1L);
		ingredient.setName("Миндаль");
		ingredient.setComponents(listDboComponents());
		return Optional.of(ingredient);
	}
	
	public static List<Component> listDboComponents() {
		final List<Component> components = new ArrayList<>();
		components.add(dboCalciumComponent());
		components.add(dboIronComponent());
		return components;
	}
	
	public static Component dboCalciumComponent() {
		final Component component = new Component();
		component.setId(1L);
		component.setName("Кальций");
		return component;
	}
	
	public static Component dboIronComponent() {
		final Component component = new Component();
		component.setId(2L);
		component.setName("Магний");
		return component;
	}
	
}
