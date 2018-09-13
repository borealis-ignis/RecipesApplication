package com.recipes.appl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dbo.Component;
import com.recipes.appl.model.dbo.Ingredient;
import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.errors.IngredientError;
import com.recipes.appl.repository.ComponentsDAO;
import com.recipes.appl.repository.IngredientsDAO;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientsServiceTest {
	
	@MockBean
	private IngredientsDAO ingredientsDAO;
	
	@MockBean
	private ComponentsDAO componentsDAO;
	
	@Autowired
	private IngredientsService ingredientsService;
	
	
	@Test
	public void getIngredients() {
		final List<Ingredient> dboIngredients = MockData.listDboIngredientsWithComponents();
		
		when(ingredientsDAO.selectIngredients()).thenReturn(dboIngredients);
		
		final List<IngredientDto> dtoIngredients = ingredientsService.getIngredients();
		
		assertEquals(dtoIngredients.size(), dboIngredients.size());
		assertTrue(dtoIngredients.size() > 0);
		
		for (int i = 0; i < dtoIngredients.size(); i++) {
			final IngredientDto dtoIngredient = dtoIngredients.get(i);
			final Ingredient dboIngredient = dboIngredients.get(i);
			
			assertEquals(dboIngredient.getId(), dtoIngredient.getId());
			assertEquals(dboIngredient.getName(), dtoIngredient.getName());
			assertNull(dtoIngredient.getIngredientNameId());
			assertNull(dtoIngredient.getCount());
			assertNull(dtoIngredient.getMeasure());
		}
	}
	
	@Test
	public void getComponents() {
		final List<Component> dboComponents = MockData.listDboComponents();
		
		when(componentsDAO.findAll()).thenReturn(dboComponents);
		
		final List<ComponentDto> dtoComponents = ingredientsService.getComponents();
		
		assertEquals(dtoComponents.size(), dboComponents.size());
		assertTrue(dtoComponents.size() > 0);
		
		for (int i = 0; i < dtoComponents.size(); i++) {
			final ComponentDto dtoComponent = dtoComponents.get(i);
			final Component dboComponent = dboComponents.get(i);
			
			assertEquals(dboComponent.getId(), dtoComponent.getId());
			assertEquals(dboComponent.getName(), dtoComponent.getName());
		}
	}
	
	@Test
	public void getComponentsByIngredientId() {
		final Optional<Ingredient> dboIngredient = MockData.optionalDboIngredientWithComponents(); 
		final List<Component> dboComponents = dboIngredient.get().getComponents();
		final Long id = 1L;
		
		when(ingredientsDAO.findById(id)).thenReturn(dboIngredient);
		
		final List<ComponentDto> dtoComponents = ingredientsService.getComponents(id);
		
		assertEquals(dtoComponents.size(), dboComponents.size());
		assertTrue(dtoComponents.size() > 0);
		
		for (int i = 0; i < dtoComponents.size(); i++) {
			final ComponentDto dtoComponent = dtoComponents.get(i);
			final Component dboComponent = dboComponents.get(i);
			
			assertEquals(dboComponent.getId(), dtoComponent.getId());
			assertEquals(dboComponent.getName(), dtoComponent.getName());
		}
	}
	
	@Test
	public void saveNewIngredient() {
		final Ingredient dboIngredient = MockData.dboAlmondIngredientWithComponents(true);
		
		when(ingredientsDAO.saveAndFlush(MockData.dboAlmondIngredientWithComponents(false))).thenReturn(dboIngredient);
		
		final IngredientDto dtoIngredient = ingredientsService.saveIngredient(MockData.dtoAlmondIngredient(true, false));
		
		assertEquals(dboIngredient.getId(), dtoIngredient.getId());
		assertEquals(dboIngredient.getName(), dtoIngredient.getName());
		assertNull(dtoIngredient.getIngredientNameId());
		assertNull(dtoIngredient.getCount());
		assertNull(dtoIngredient.getMeasure());
	}
	
	@Test
	public void updateIngredient() {
		final Ingredient dboIngredient = MockData.dboAlmondIngredientWithComponents(true);
		
		when(ingredientsDAO.saveAndFlush(dboIngredient)).thenReturn(dboIngredient);
		
		final IngredientDto dtoIngredient = ingredientsService.saveIngredient(MockData.dtoAlmondIngredient(true, true));
		
		assertEquals(dboIngredient.getId(), dtoIngredient.getId());
		assertEquals(dboIngredient.getName(), dtoIngredient.getName());
		assertNull(dtoIngredient.getIngredientNameId());
		assertNull(dtoIngredient.getCount());
		assertNull(dtoIngredient.getMeasure());
	}
	
	@Test
	public void deleteIngredient() {
		final Long id = 1L;
		
		final IngredientDto dtoIngredient = ingredientsService.deleteIngredient(id);
		
		assertEquals(dtoIngredient.getId(), id);
		assertNull(dtoIngredient.getName());
		assertNull(dtoIngredient.getIngredientNameId());
		assertNull(dtoIngredient.getCount());
		assertNull(dtoIngredient.getMeasure());
	}
	
	@Test
	public void validateCorrectIngredient() {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, true);
		
		final ResponseEntity<IngredientDto> validationResult = ingredientsService.validateIngredient(ingredient);
		
		assertNull(validationResult);
	}
	
	@Test
	public void validateCorrectNewIngredient() {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, false);
		
		when(ingredientsDAO.findAllByName(ingredient.getName())).thenReturn(Collections.emptyList());
		
		final ResponseEntity<IngredientDto> validationResult = ingredientsService.validateIngredient(ingredient);
		
		assertNull(validationResult);
	}
	
	@Test
	public void validateIngredientWithoutName() {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, true);
		ingredient.setName(null);
		
		final ResponseEntity<IngredientDto> validationResult = ingredientsService.validateIngredient(ingredient);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof IngredientError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void validateNewIngredientWithExistingName() {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, false);
		
		when(ingredientsDAO.findAllByName(ingredient.getName())).thenReturn(MockData.listDboIngredientsWithComponents());
		
		final ResponseEntity<IngredientDto> validationResult = ingredientsService.validateIngredient(ingredient);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof IngredientError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
