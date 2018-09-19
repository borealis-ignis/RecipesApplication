package com.recipes.appl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.recipes.appl.converter.impl.ComponentConverter;
import com.recipes.appl.converter.impl.IngredientConverter;
import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.errors.IngredientError;
import com.recipes.appl.repository.ComponentsDAO;
import com.recipes.appl.repository.IngredientsDAO;

/**
 * @author Kastalski Sergey
 */
@Service
public class IngredientsService extends AbstractService {
	
	private IngredientsDAO ingredientsDAO;
	
	private ComponentsDAO componentsDAO;
	
	private ComponentConverter componentConverter;
	
	private IngredientConverter ingredientConverter;
	
	
	@Autowired
	public IngredientsService(
			final IngredientsDAO ingredientsDAO, 
			final ComponentsDAO componentsDAO, 
			final ComponentConverter componentConverter, 
			final IngredientConverter ingredientConverter) {
		this.ingredientsDAO = ingredientsDAO;
		this.componentsDAO = componentsDAO;
		this.componentConverter = componentConverter;
		this.ingredientConverter = ingredientConverter;
	}
	
	
	public List<IngredientDto> getIngredients() {
		return ingredientConverter.convertDboToDto(ingredientsDAO.selectIngredients());
	}
	
	public List<ComponentDto> getComponents() {
		return componentConverter.convertDboToDto(componentsDAO.findAll());
	}
	
	public List<ComponentDto> getComponents(final Long id) {
		return componentConverter.convertDboToDto(ingredientsDAO.findById(id).orElse(new IngredientDbo()).getComponents());
	}
	
	@Transactional
	public IngredientDto saveIngredient(final IngredientDto ingredient) {
		final List<ComponentDto> allComponents = ingredient.getComponents();
		if (!CollectionUtils.isEmpty(allComponents)) {
			final Predicate<ComponentDto> componentIdNull = c -> c.getId() == null;
			final List<ComponentDto> newComponents = allComponents.stream().filter(componentIdNull).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(newComponents)) {
				final List<ComponentDto> savedComponents = componentConverter.convertDboToDto(componentsDAO.saveAll(componentConverter.convertDtoToDbo(newComponents)));
				allComponents.removeIf(componentIdNull);
				allComponents.addAll(savedComponents);
			}
			
			if (ingredient.getId() == null) {
				ingredient.setComponents(new ArrayList<>());
				final IngredientDto savedIngredient = ingredientConverter.convertDboToDto(ingredientsDAO.saveAndFlush(ingredientConverter.convertDtoToDbo(ingredient)));
				ingredient.setId(savedIngredient.getId());
			}
			
			ingredient.setComponents(allComponents);
		}
		
		return ingredientConverter.convertDboToDto(ingredientsDAO.saveAndFlush(ingredientConverter.convertDtoToDbo(ingredient)));
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
			final List<IngredientDbo> foundIngredients = ingredientsDAO.findAllByName(ingredientName);
			if (!foundIngredients.isEmpty()) {
				return getErrorResponseMessage("admin.ingredient.error.notunique.name", IngredientError.class);
			}
		}
		
		return null;
	}
	
}
