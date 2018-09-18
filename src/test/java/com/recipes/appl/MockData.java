package com.recipes.appl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.recipes.appl.model.dbo.ComponentDbo;
import com.recipes.appl.model.dbo.DishTypeDbo;
import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dbo.IngredientMeasureDbo;
import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.RecipeDto;

/**
 * @author Kastalski Sergey
 */
public class MockData {
	
	// dto data
	public static List<IngredientDto> listDtoIngredients(final boolean withComponents) {
		final List<IngredientDto> ingredients = new ArrayList<>();
		ingredients.add(dtoAlmondIngredient(withComponents, true));
		ingredients.add(dtoCornIngredient(withComponents, true));
		return ingredients;
	}
	
	public static IngredientDto dtoAlmondIngredient(final boolean withComponents, final boolean withId) {
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
	
	public static IngredientDto dtoCornIngredient(final boolean withComponents, final boolean withId) {
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
	
	
	public static List<DishTypeDto> listDtoDishTypes() {
		final List<DishTypeDto> dishTypes = new ArrayList<>();
		dishTypes.add(dtoSoupDishType());
		dishTypes.add(dtoSauceDishType());
		return dishTypes;
	}
	
	public static DishTypeDto dtoSoupDishType() {
		final DishTypeDto dishType = new DishTypeDto();
		dishType.setId(1L);
		dishType.setName("Суп");
		return dishType;
	}
	
	public static DishTypeDto dtoSauceDishType() {
		final DishTypeDto dishType = new DishTypeDto();
		dishType.setId(2L);
		dishType.setName("Соус");
		return dishType;
	}
	
	
	public static List<IngredientMeasureDto> listDtoIngredientMeasures() {
		final List<IngredientMeasureDto> ingredientMeasures = new ArrayList<>();
		ingredientMeasures.add(dtoMlIngredientMeasure());
		ingredientMeasures.add(dtoSpoonsIngredientMeasure());
		return ingredientMeasures;
	}
	
	public static IngredientMeasureDto dtoMlIngredientMeasure() {
		final IngredientMeasureDto ingredientMeasure = new IngredientMeasureDto();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		return ingredientMeasure;
	}
	
	public static IngredientMeasureDto dtoSpoonsIngredientMeasure() {
		final IngredientMeasureDto ingredientMeasure = new IngredientMeasureDto();
		ingredientMeasure.setId(2L);
		ingredientMeasure.setName("чайных ложек");
		return ingredientMeasure;
	}
	
	
	public static List<RecipeDto> listDtoRecipes() {
		final List<RecipeDto> recipes = new ArrayList<>();
		recipes.add(dtoSauceRecipe(true));
		recipes.add(dtoSoupRecipe(true));
		return recipes;
	}
	
	public static RecipeDto dtoSauceRecipe(final boolean withId) {
		final RecipeDto dtoRecipe = new RecipeDto();
		if (withId) {
			dtoRecipe.setId(1L);
		}
		dtoRecipe.setDishType(dtoSauceDishType());
		dtoRecipe.setName("Recipe#1");
		dtoRecipe.setDescription("Description");
		
		final List<IngredientDto> ingredients = new ArrayList<>();
		final IngredientDto ingredient = dtoAlmondIngredient(false, true);
		ingredient.setIngredientNameId(1L);
		ingredient.setCount(2.0);
		ingredient.setMeasure(dtoMlIngredientMeasure());
		ingredients.add(ingredient);
		
		dtoRecipe.setIngredients(ingredients);
		return dtoRecipe;
	}
	
	public static RecipeDto dtoSoupRecipe(final boolean withId) {
		final RecipeDto dtoRecipe = new RecipeDto();
		if (withId) {
			dtoRecipe.setId(2L);
		}
		dtoRecipe.setDishType(dtoSauceDishType());
		dtoRecipe.setName("Recipe#2");
		dtoRecipe.setDescription("Description");
		
		final List<IngredientDto> ingredients = new ArrayList<>();
		final IngredientDto ingredient = dtoCornIngredient(false, true);
		ingredient.setIngredientNameId(1L);
		ingredient.setCount(2.0);
		ingredient.setMeasure(dtoMlIngredientMeasure());
		ingredients.add(ingredient);
		
		dtoRecipe.setIngredients(ingredients);
		return dtoRecipe;
	}
	
	// dbo data
	public static List<IngredientDbo> listDboIngredientsWithComponents() {
		final List<IngredientDbo> ingredients = new ArrayList<>();
		ingredients.add(dboAlmondIngredientWithComponents(true));
		return ingredients;
	}
	
	public static Optional<IngredientDbo> optionalDboIngredientWithComponents() {
		return Optional.of(dboAlmondIngredientWithComponents(true));
	}
	
	public static IngredientDbo dboAlmondIngredientWithComponents(final boolean withId) {
		final IngredientDbo ingredient = new IngredientDbo();
		if (withId) {
			ingredient.setId(1L);
		}
		ingredient.setName("Миндаль");
		ingredient.setComponents(listDboComponents());
		return ingredient;
	}
	
	public static IngredientDbo dboAlmondIngredientWithComponentsWithoutIds() {
		final IngredientDbo almond = MockData.dboAlmondIngredientWithoutComponents(false);
		almond.setComponents(MockData.listDboComponentsWithoutIds());
		return almond;
	}
	
	public static IngredientDbo dboAlmondIngredientWithoutComponents(final boolean withId) {
		final IngredientDbo ingredient = new IngredientDbo();
		if (withId) {
			ingredient.setId(1L);
		}
		ingredient.setName("Миндаль");
		ingredient.setComponents(new ArrayList<>());
		return ingredient;
	}
	
	public static List<ComponentDbo> listDboComponents() {
		final List<ComponentDbo> components = new ArrayList<>();
		components.add(dboCalciumComponent(true));
		components.add(dboIronComponent(true));
		return components;
	}
	
	public static List<ComponentDbo> listDboComponentsWithoutIds() {
		final List<ComponentDbo> components = new ArrayList<>();
		components.add(dboCalciumComponent(false));
		components.add(dboIronComponent(false));
		return components;
	}
	
	public static ComponentDbo dboCalciumComponent(final boolean withId) {
		final ComponentDbo component = new ComponentDbo();
		if (withId) {
			component.setId(1L);
		}
		component.setName("Кальций");
		return component;
	}
	
	public static ComponentDbo dboIronComponent(final boolean withId) {
		final ComponentDbo component = new ComponentDbo();
		if (withId) {
			component.setId(2L);
		}
		component.setName("Магний");
		return component;
	}
	
	
	
	public static List<DishTypeDbo> listDboDishTypes() {
		final List<DishTypeDbo> dishTypes = new ArrayList<>();
		dishTypes.add(dboSoupDishType());
		dishTypes.add(dboSauceDishType());
		return dishTypes;
	}
	
	public static DishTypeDbo dboSoupDishType() {
		final DishTypeDbo dishType = new DishTypeDbo();
		dishType.setId(1L);
		dishType.setName("Суп");
		return dishType;
	}
	
	public static DishTypeDbo dboSauceDishType() {
		final DishTypeDbo dishType = new DishTypeDbo();
		dishType.setId(2L);
		dishType.setName("Соус");
		return dishType;
	}
	
	
	public static List<IngredientMeasureDbo> listDboIngredientMeasures() {
		final List<IngredientMeasureDbo> ingredientMeasures = new ArrayList<>();
		ingredientMeasures.add(dboMlIngredientMeasure());
		ingredientMeasures.add(dboSpoonsIngredientMeasure());
		return ingredientMeasures;
	}
	
	public static IngredientMeasureDbo dboMlIngredientMeasure() {
		final IngredientMeasureDbo ingredientMeasure = new IngredientMeasureDbo();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		return ingredientMeasure;
	}
	
	public static IngredientMeasureDbo dboSpoonsIngredientMeasure() {
		final IngredientMeasureDbo ingredientMeasure = new IngredientMeasureDbo();
		ingredientMeasure.setId(2L);
		ingredientMeasure.setName("чайных ложек");
		return ingredientMeasure;
	}
	
	
	public static List<RecipeDbo> listDboRecipes() {
		final List<RecipeDbo> recipes = new ArrayList<>();
		recipes.add(dboSauceRecipe(true));
		recipes.add(dboSoupRecipe(true));
		return recipes;
	}
	
	public static RecipeDbo dboSauceRecipe(final boolean withId) {
		final RecipeDbo recipe = new RecipeDbo();
		if (withId) {
			recipe.setId(1L);
		}
		recipe.setDishType(dboSauceDishType());
		recipe.setName("Recipe#1");
		recipe.setDescription("Description");
		
		final List<RecipeIngredientDbo> recipeIngredients = new ArrayList<>();
		final RecipeIngredientDbo ingredient = new RecipeIngredientDbo();
		ingredient.setId(1L);
		ingredient.setIngredient(dboAlmondIngredientWithoutComponents(true));
		ingredient.setCount(2.0);
		
		IngredientMeasureDbo ingredientMeasure = new IngredientMeasureDbo();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		ingredient.setIngredientMeasure(ingredientMeasure);
		
		recipeIngredients.add(ingredient);
		
		recipe.setRecipeIngredients(recipeIngredients);
		return recipe;
	}
	
	public static Optional<RecipeDbo> optionalDboSauceRecipe(final boolean withId) {
		return Optional.of(dboSauceRecipe(withId));
	}
	
	public static RecipeDbo dboSoupRecipe(final boolean withId) {
		final RecipeDbo recipe = new RecipeDbo();
		if (withId) {
			recipe.setId(2L);
		}
		recipe.setDishType(dboSoupDishType());
		recipe.setName("Recipe#2");
		recipe.setDescription("Description");
		
		final List<RecipeIngredientDbo> recipeIngredients = new ArrayList<>();
		final RecipeIngredientDbo ingredient = new RecipeIngredientDbo();
		ingredient.setId(2L);
		ingredient.setIngredient(dboAlmondIngredientWithoutComponents(true));
		ingredient.setCount(2.0);
		
		IngredientMeasureDbo ingredientMeasure = new IngredientMeasureDbo();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		ingredient.setIngredientMeasure(ingredientMeasure);
		
		recipeIngredients.add(ingredient);
		
		recipe.setRecipeIngredients(recipeIngredients);
		return recipe;
	}
}
