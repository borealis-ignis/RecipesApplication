package com.recipes.appl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.recipes.appl.model.dbo.Ingredient;
import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.errors.IngredientError;
import com.recipes.appl.repository.ComponentsDAO;
import com.recipes.appl.repository.IngredientsDAO;
import com.recipes.appl.utils.converters.ComponentsConverter;
import com.recipes.appl.utils.converters.IngredientsConverter;

/**
 * @author Kastalski Sergey
 */
@Service
public class IngredientsService extends AbstractService {
	
	@Autowired
	private IngredientsDAO ingredientsDAO;
	
	@Autowired
	private ComponentsDAO componentsDAO;
	
	
	public List<IngredientDto> getIngredients() {
		return IngredientsConverter.convertDboToDto(ingredientsDAO.selectIngredients());
	}
	
	public List<ComponentDto> getComponents() {
		return ComponentsConverter.convertDboToDto(componentsDAO.findAll());
	}
	
	public List<ComponentDto> getComponents(final Long id) {
		return ComponentsConverter.convertDboToDto(ingredientsDAO.findById(id).orElse(new Ingredient()).getComponents());
	}
	
	public IngredientDto saveIngredient(final IngredientDto ingredient) {
		return IngredientsConverter.convertDboToDto(ingredientsDAO.saveAndFlush(IngredientsConverter.convertDtoToDbo(ingredient)));
	}
	
	public IngredientDto deleteIngredient(final Long id) {
		ingredientsDAO.deleteById(id);
		
		final IngredientDto dto = new IngredientDto();
		dto.setId(id);
		return dto;
	}
	
	public ResponseEntity<IngredientDto> validateIngredient(final IngredientDto ingredient) {
		final String ingredientName = ingredient.getName();
		if (StringUtils.isEmpty(ingredientName)) {
			return getErrorResponseMessage("admin.ingredient.error.noname", IngredientError.class);
		}
		
		// add new ingredient
		if (ingredient.getId() == null) {
			final List<Ingredient> foundIngredients = ingredientsDAO.findAllByName(ingredientName);
			if (!foundIngredients.isEmpty()) {
				return getErrorResponseMessage("admin.ingredient.error.notunique.name", IngredientError.class);
			}
		}
		
		return null;
	}
	
}
