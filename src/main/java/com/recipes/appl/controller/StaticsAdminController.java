package com.recipes.appl.controller;

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

import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.errors.StaticError;
import com.recipes.appl.model.dto.errors.StaticItemError;
import com.recipes.appl.service.RecipesService;

/**
 * @author Kastalski Sergey
 */
@Controller
@RequestMapping("/admin")
public class StaticsAdminController {
	
	private static Logger logger = LoggerFactory.getLogger(StaticsAdminController.class);
	
	private RecipesService recipesService;
	
	
	@Autowired
	public StaticsAdminController(final RecipesService recipesService) {
		this.recipesService = recipesService;
	}
	
	
	@GetMapping(path="/statics")
	public String getAdminRecipesPage(final Map<String, Object> model) {
		model.put("dishTypes", recipesService.getDishTypes());
		model.put("ingredientMeasures", recipesService.getIngredientMeasures());
		return "admin/statics";
	}
	
	@PostMapping(path="/dishtype")
	public ResponseEntity<DishTypeDto> saveDishType(@RequestBody(required = true) final DishTypeDto dishType) {
		final ResponseEntity<DishTypeDto> dishTypeError = recipesService.validateDishType(dishType);
		if (dishTypeError != null) {
			return dishTypeError;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(recipesService.saveDishType(dishType));
	}
	
	@PostMapping(path="/measure")
	public ResponseEntity<IngredientMeasureDto> saveMeasure(@RequestBody(required = true) final IngredientMeasureDto measure) {
		final ResponseEntity<IngredientMeasureDto> measureError = recipesService.validateMeasure(measure);
		if (measureError != null) {
			return measureError;
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(recipesService.saveIngredientMeasure(measure));
	}
	
	@DeleteMapping(path = "/dishtype")
	public ResponseEntity<DishTypeDto> deleteDishtype(@RequestParam(required = true, name = "id") final Long id) {
		final ResponseEntity<DishTypeDto> dishtypeError = recipesService.validateDishtypeBeforeDelete(id);
		if (dishtypeError != null) {
			return dishtypeError;
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(recipesService.deleteDishType(id));
	}
	
	@DeleteMapping(path = "/measure")
	public ResponseEntity<IngredientMeasureDto> deleteMeasure(@RequestParam(required = true, name = "id") final Long id) {
		final ResponseEntity<IngredientMeasureDto> measureError = recipesService.validateMeasureBeforeDelete(id);
		if (measureError != null) {
			return measureError;
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(recipesService.deleteMeasure(id));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StaticItemError> handleError(final Exception ex) {
		logger.error("Internal server error", ex);
		return new ResponseEntity<StaticItemError>(new StaticError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
