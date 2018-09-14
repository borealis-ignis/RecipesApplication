package com.recipes.appl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.repository.DishTypesDAO;
import com.recipes.appl.repository.IngredientMeasuresDAO;
import com.recipes.appl.repository.RecipesDAO;
import com.recipes.appl.utils.converters.DishTypesConverter;
import com.recipes.appl.utils.converters.IngredientMeasuresConverter;
import com.recipes.appl.utils.converters.RecipesConverter;

/**
 * @author Kastalski Sergey
 */
@Service
public class RecipesService extends AbstractService {
	
	private DishTypesDAO dishTypesDAO;
	
	private IngredientMeasuresDAO ingredientMeasuresDAO;
	
	private RecipesDAO recipesDAO;
	
	@Autowired
	public RecipesService(final DishTypesDAO dishTypesDAO, final IngredientMeasuresDAO ingredientMeasuresDAO, final RecipesDAO recipesDAO) {
		this.dishTypesDAO = dishTypesDAO;
		this.ingredientMeasuresDAO = ingredientMeasuresDAO;
		this.recipesDAO = recipesDAO;
	}
	
	
	public List<DishTypeDto> getDishTypes() {
		return DishTypesConverter.convertDboToDto(dishTypesDAO.findAllByOrderByIdAsc());
	}
	
	public List<IngredientMeasureDto> getIngredientMeasures() {
		return IngredientMeasuresConverter.convertDboToDto(ingredientMeasuresDAO.findAllByOrderByIdAsc());
	}
	
	public List<RecipeDto> getRecipes() {
		return RecipesConverter.convertDboToDto(recipesDAO.findAll());
	}
	
	public List<RecipeDto> searchRecipes(Map<String, Object> params) {
		//TODO
		return new ArrayList<>();
	}

	public RecipeDto getRecipe(final Long id) {
		return RecipesConverter.convertDboToDto(recipesDAO.findById(id).orElse(new RecipeDbo()));
	}
	
	public RecipeDto saveRecipe(RecipeDto recipe) {
		return RecipesConverter.convertDboToDto(recipesDAO.saveAndFlush(RecipesConverter.convertDtoToDbo(recipe)));
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
