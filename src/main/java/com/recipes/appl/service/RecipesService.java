package com.recipes.appl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.recipes.appl.converter.impl.DishTypeConverter;
import com.recipes.appl.converter.impl.IngredientMeasureConverter;
import com.recipes.appl.converter.impl.RecipeConverter;
import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.repository.DishTypesDAO;
import com.recipes.appl.repository.IngredientMeasuresDAO;
import com.recipes.appl.repository.RecipesDAO;

/**
 * @author Kastalski Sergey
 */
@Service
public class RecipesService extends AbstractService {
	
	private DishTypesDAO dishTypesDAO;
	
	private IngredientMeasuresDAO ingredientMeasuresDAO;
	
	private RecipesDAO recipesDAO;
	
	private DishTypeConverter dishTypeConverter;
	
	private RecipeConverter recipeConverter;
	
	private IngredientMeasureConverter ingredientMeasureConverter;
	
	@Autowired
	public RecipesService(
			final DishTypesDAO dishTypesDAO, 
			final IngredientMeasuresDAO ingredientMeasuresDAO, 
			final RecipesDAO recipesDAO, 
			final DishTypeConverter dishTypeConverter,
			final RecipeConverter recipeConverter,
			final IngredientMeasureConverter ingredientMeasureConverter) {
		this.dishTypesDAO = dishTypesDAO;
		this.ingredientMeasuresDAO = ingredientMeasuresDAO;
		this.recipesDAO = recipesDAO;
		this.dishTypeConverter = dishTypeConverter;
		this.recipeConverter = recipeConverter;
		this.ingredientMeasureConverter = ingredientMeasureConverter;
	}
	
	
	public List<DishTypeDto> getDishTypes() {
		return dishTypeConverter.convertDboToDto(dishTypesDAO.findAllByOrderByIdAsc());
	}
	
	public List<IngredientMeasureDto> getIngredientMeasures() {
		return ingredientMeasureConverter.convertDboToDto(ingredientMeasuresDAO.findAllByOrderByIdAsc());
	}
	
	public List<RecipeDto> getRecipes() {
		return recipeConverter.convertDboToDto(recipesDAO.findAll());
	}
	
	public List<RecipeDto> searchRecipes(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList) {
		return recipeConverter.convertDboToDto(recipesDAO.findAllByFilterParams(namePart, dishTypeId, ingredientIdList, componentIdList));
	}

	public RecipeDto getRecipe(final Long id) {
		return recipeConverter.convertDboToDto(recipesDAO.findById(id).orElse(new RecipeDbo()));
	}
	
	public RecipeDto saveRecipe(final RecipeDto recipe) {
		return recipeConverter.convertDboToDto(recipesDAO.saveAndFlush(recipeConverter.convertDtoToDbo(recipe)));
	}
	
	public RecipeDto deleteRecipe(final Long id) {
		recipesDAO.deleteById(id);
		
		final RecipeDto dto = new RecipeDto();
		dto.setId(id);
		return dto;
	}
	
	public ResponseEntity<RecipeDto> validateRecipe(final RecipeDto recipe) {
		final String recipeName = recipe.getName();
		if (StringUtils.isEmpty(recipeName)) {
			return getErrorResponseMessage("admin.recipe.error.noname", RecipeError.class);
		}
		
		final DishTypeDto dishType = recipe.getDishType();
		if (dishType == null || dishType.getId() == null) {
			return getErrorResponseMessage("admin.recipe.error.nodishtype", RecipeError.class);
		}
		
		// add new recipe
		if (recipe.getId() == null) {
			final List<RecipeDbo> foundRecipes = recipesDAO.findAllByNameAndDishType(recipeName, dishType.getId());
			if (!foundRecipes.isEmpty()) {
				return getErrorResponseMessage("admin.recipe.error.notunique.name", RecipeError.class);
			}
		}
		
		if (CollectionUtils.isEmpty(recipe.getIngredients())) {
			return getErrorResponseMessage("admin.recipe.error.noingredients", RecipeError.class);
		}
		
		return null;
	}
	
}
