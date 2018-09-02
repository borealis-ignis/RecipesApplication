package com.recipes.appl.controller;

import java.util.List;
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

import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.errors.IngredientError;
import com.recipes.appl.service.IngredientsService;

/**
 * @author Kastalski Sergey
 */
@Controller
@RequestMapping("/admin")
public class IngredientsAdminController {
	
	@Autowired
	private IngredientsService ingredientsService;
	
	
	@GetMapping(path="/ingredients")
	public String getAdminIngredientsPage(final Map<String, Object> model) {
		model.put("ingredients", ingredientsService.getIngredients());
		model.put("components", ingredientsService.getComponents());
		return "admin/ingredients";
	}
	
	@GetMapping(path="/ingredient/components")
	public ResponseEntity<List<ComponentDto>> getComponents(@RequestParam(required = true, name = "id") Long componentId) {
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.getComponents(componentId));
	}
	
	@GetMapping(path="/ingredient/allcomponents")
	public ResponseEntity<List<ComponentDto>> getAllComponents() {
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.getComponents());
	}
	
	@PostMapping(path="/ingredient/save")
	public ResponseEntity<IngredientDto> saveIngredient(@RequestBody(required = true) IngredientDto ingredient) {
		final ResponseEntity<IngredientDto> ingredientError = ingredientsService.validateIngredient(ingredient);
		if (ingredientError != null) {
			return ingredientError;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.saveIngredient(ingredient));
	}
	
	@DeleteMapping(path="/ingredient/delete")
	public ResponseEntity<IngredientDto> deleteIngredient(@RequestParam(required = true, name = "id") Long id) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ingredientsService.deleteIngredient(id));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<IngredientError> handleError(final Exception ex) {
		return new ResponseEntity<IngredientError>(new IngredientError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
