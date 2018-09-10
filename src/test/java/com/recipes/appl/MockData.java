package com.recipes.appl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.recipes.appl.model.dbo.Component;
import com.recipes.appl.model.dbo.DishType;
import com.recipes.appl.model.dbo.Ingredient;
import com.recipes.appl.model.dbo.IngredientMeasure;
import com.recipes.appl.model.dbo.Recipe;
import com.recipes.appl.model.dbo.RecipeIngredient;
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
	public static List<Ingredient> listDboIngredientsWithComponents() {
		final List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(dboAlmondIngredientWithComponents(true));
		return ingredients;
	}
	
	public static Optional<Ingredient> optionalDboIngredientWithComponents() {
		return Optional.of(dboAlmondIngredientWithComponents(true));
	}
	
	public static Ingredient dboAlmondIngredientWithComponents(final boolean withId) {
		final Ingredient ingredient = new Ingredient();
		if (withId) {
			ingredient.setId(1L);
		}
		ingredient.setName("Миндаль");
		ingredient.setComponents(listDboComponents());
		return ingredient;
	}
	
	public static Ingredient dboAlmondIngredientWithoutComponents(final boolean withId) {
		final Ingredient ingredient = new Ingredient();
		if (withId) {
			ingredient.setId(1L);
		}
		ingredient.setName("Миндаль");
		return ingredient;
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
	
	
	
	public static List<DishType> listDboDishTypes() {
		final List<DishType> dishTypes = new ArrayList<>();
		dishTypes.add(dboSoupDishType());
		dishTypes.add(dboSauceDishType());
		return dishTypes;
	}
	
	public static DishType dboSoupDishType() {
		final DishType dishType = new DishType();
		dishType.setId(1L);
		dishType.setName("Суп");
		return dishType;
	}
	
	public static DishType dboSauceDishType() {
		final DishType dishType = new DishType();
		dishType.setId(2L);
		dishType.setName("Соус");
		return dishType;
	}
	
	
	public static List<IngredientMeasure> listDboIngredientMeasures() {
		final List<IngredientMeasure> ingredientMeasures = new ArrayList<>();
		ingredientMeasures.add(dboMlIngredientMeasure());
		ingredientMeasures.add(dboSpoonsIngredientMeasure());
		return ingredientMeasures;
	}
	
	public static IngredientMeasure dboMlIngredientMeasure() {
		final IngredientMeasure ingredientMeasure = new IngredientMeasure();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		return ingredientMeasure;
	}
	
	public static IngredientMeasure dboSpoonsIngredientMeasure() {
		final IngredientMeasure ingredientMeasure = new IngredientMeasure();
		ingredientMeasure.setId(2L);
		ingredientMeasure.setName("чайных ложек");
		return ingredientMeasure;
	}
	
	
	public static List<Recipe> listDboRecipes() {
		final List<Recipe> recipes = new ArrayList<>();
		recipes.add(dboSauceRecipe(true));
		recipes.add(dboSoupRecipe(true));
		return recipes;
	}
	
	public static Recipe dboSauceRecipe(final boolean withId) {
		final Recipe recipe = new Recipe();
		if (withId) {
			recipe.setId(1L);
		}
		recipe.setDishType(dboSauceDishType());
		recipe.setName("Recipe#1");
		recipe.setDescription("Description");
		
		final List<RecipeIngredient> recipeIngredients = new ArrayList<>();
		final RecipeIngredient ingredient = new RecipeIngredient();
		ingredient.setId(1L);
		ingredient.setIngredient(dboAlmondIngredientWithoutComponents(true));
		ingredient.setCount(2.0);
		
		IngredientMeasure ingredientMeasure = new IngredientMeasure();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		ingredient.setIngredientMeasure(ingredientMeasure);
		
		recipeIngredients.add(ingredient);
		
		recipe.setRecipeIngredients(recipeIngredients);
		return recipe;
	}
	
	public static Optional<Recipe> optionalDboSauceRecipe(final boolean withId) {
		return Optional.of(dboSauceRecipe(withId));
	}
	
	public static Recipe dboSoupRecipe(final boolean withId) {
		final Recipe recipe = new Recipe();
		if (withId) {
			recipe.setId(2L);
		}
		recipe.setDishType(dboSoupDishType());
		recipe.setName("Recipe#2");
		recipe.setDescription("Description");
		
		final List<RecipeIngredient> recipeIngredients = new ArrayList<>();
		final RecipeIngredient ingredient = new RecipeIngredient();
		ingredient.setId(2L);
		ingredient.setIngredient(dboAlmondIngredientWithoutComponents(true));
		ingredient.setCount(2.0);
		
		IngredientMeasure ingredientMeasure = new IngredientMeasure();
		ingredientMeasure.setId(1L);
		ingredientMeasure.setName("мл");
		ingredient.setIngredientMeasure(ingredientMeasure);
		
		recipeIngredients.add(ingredient);
		
		recipe.setRecipeIngredients(recipeIngredients);
		return recipe;
	}
}
