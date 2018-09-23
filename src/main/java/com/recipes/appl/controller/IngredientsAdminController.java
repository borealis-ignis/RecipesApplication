package com.recipes.appl.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger logger = LoggerFactory.getLogger(IngredientsAdminController.class);
	
	private IngredientsService ingredientsService;
	
	@Autowired
	public IngredientsAdminController(final IngredientsService ingredientsService) {
		this.ingredientsService = ingredientsService;
	}
	
	
	@GetMapping(path="/ingredients")
	public String getAdminIngredientsPage(final Map<String, Object> model) {
		model.put("ingredients", ingredientsService.getIngredients());
		model.put("components", ingredientsService.getComponents());
		return "admin/ingredients";
	}
	
	@GetMapping(path="/ingredient/components")
	public ResponseEntity<List<ComponentDto>> getComponents(@RequestParam(required = true, name = "id") final Long ingredientId) {
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.getComponents(ingredientId));
	}
	
	@GetMapping(path="/ingredient/allcomponents")
	public ResponseEntity<List<ComponentDto>> getAllComponents() {
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.getComponents());
	}
	
	@PostMapping(path="/ingredient")
	public ResponseEntity<IngredientDto> saveIngredient(@RequestBody(required = true) final IngredientDto ingredient) {
		final ResponseEntity<IngredientDto> ingredientError = ingredientsService.validateIngredient(ingredient);
		if (ingredientError != null) {
			return ingredientError;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ingredientsService.saveIngredient(ingredient));
	}
	
	@DeleteMapping(path="/ingredient")
	public ResponseEntity<IngredientDto> deleteIngredient(@RequestParam(required = true, name = "id") final Long id) {
		final ResponseEntity<IngredientDto> ingredientError = ingredientsService.validateIngredientBeforeDelete(id);
		if (ingredientError != null) {
			return ingredientError;
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ingredientsService.deleteIngredient(id));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<IngredientError> handleError(final Exception ex) {
		logger.error("Internal server error", ex);
		return new ResponseEntity<IngredientError>(new IngredientError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
