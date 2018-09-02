package com.recipes.appl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.service.IngredientsService;
import com.recipes.appl.service.RecipesService;

/**
 * @author Kastalski Sergey
 */
@Controller
@RequestMapping("/admin")
public class RecipesAdminController {
	
	@Autowired
	private IngredientsService ingredientsService;
	
	@Autowired
	private RecipesService recipesService;
	
	
	@GetMapping(path="/recipes")
	public String getAdminRecipesPage(final Map<String, Object> model) {
		model.put("dishTypes", recipesService.getDishTypes());
		model.put("ingredientMeasures", recipesService.getIngredientMeasures());
		model.put("ingredients", ingredientsService.getIngredients());
		model.put("recipes", recipesService.getRecipes());
		return "admin/recipes";
	}
	
	@GetMapping(path="/recipe")
	public ResponseEntity<RecipeDto> getRecipe(@RequestParam(required = true, name = "id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(recipesService.getRecipe(id));
	}
	
	@PostMapping(path="/recipe/save")
	public ResponseEntity<RecipeDto> saveRecipe(@RequestBody(required = true) RecipeDto recipe) {
		final ResponseEntity<RecipeDto> recipeError = recipesService.validateRecipe(recipe);
		if (recipeError != null) {
			return recipeError;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(recipesService.saveRecipe(recipe));
	}
	
	@DeleteMapping(path = "/recipe/delete")
	public ResponseEntity<RecipeDto> deleteRecipe(@RequestParam(required = true, name = "id") Long id) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(recipesService.deleteRecipe(id));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<RecipeError> handleError(final Exception ex) {
		return new ResponseEntity<RecipeError>(new RecipeError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
