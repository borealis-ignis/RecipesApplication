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
import com.recipes.appl.model.dbo.DishType;
import com.recipes.appl.model.dbo.IngredientMeasure;
import com.recipes.appl.model.dbo.Recipe;
import com.recipes.appl.model.dbo.RecipeIngredient;
import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.repository.DishTypesDAO;
import com.recipes.appl.repository.IngredientMeasuresDAO;
import com.recipes.appl.repository.RecipesDAO;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipesServiceTest {
	
	@MockBean
	private DishTypesDAO dishTypesDAO;
	
	@MockBean
	private IngredientMeasuresDAO ingredientMeasuresDAO;
	
	@MockBean
	private RecipesDAO recipesDAO;
	
	@Autowired
	private RecipesService recipesService;
	
	
	@Test
	public void getDishTypes() {
		final List<DishType> dboDishTypes = MockData.listDboDishTypes();
		
		when(dishTypesDAO.findAllByOrderByIdAsc()).thenReturn(dboDishTypes);
		
		final List<DishTypeDto> dtoDishTypes = recipesService.getDishTypes();
		
		assertEquals(dboDishTypes.size(), dtoDishTypes.size());
		assertTrue(dtoDishTypes.size() > 0);
		
		for (int i = 0; i < dtoDishTypes.size(); i++) {
			final DishType dboDishType = dboDishTypes.get(i);
			final DishTypeDto dtoDishType = dtoDishTypes.get(i);
			
			assertEquals(dboDishType.getId(), dtoDishType.getId());
			assertEquals(dboDishType.getName(), dtoDishType.getName());
		}
	}
	
	@Test
	public void getIngredientMeasures() {
		final List<IngredientMeasure> dboIngredientMeasures = MockData.listDboIngredientMeasures();
		
		when(ingredientMeasuresDAO.findAllByOrderByIdAsc()).thenReturn(dboIngredientMeasures);
		
		final List<IngredientMeasureDto> dtoIngredientMeasures = recipesService.getIngredientMeasures();
		
		assertEquals(dboIngredientMeasures.size(), dtoIngredientMeasures.size());
		assertTrue(dtoIngredientMeasures.size() > 0);
		
		for (int i = 0; i < dtoIngredientMeasures.size(); i++) {
			final IngredientMeasure dboIngredientMeasure = dboIngredientMeasures.get(i);
			final IngredientMeasureDto dtoIngredientMeasure = dtoIngredientMeasures.get(i);
			
			assertEquals(dboIngredientMeasure.getId(), dtoIngredientMeasure.getId());
			assertEquals(dboIngredientMeasure.getName(), dtoIngredientMeasure.getName());
		}
	}
	
	@Test
	public void getRecipes() {
		final List<Recipe> dboRecipes = MockData.listDboRecipes();
		
		when(recipesDAO.findAll()).thenReturn(dboRecipes);
		
		final List<RecipeDto> dtoRecipes = recipesService.getRecipes();
		
		assertEquals(dboRecipes.size(), dtoRecipes.size());
		assertTrue(dtoRecipes.size() > 0);
		
		for (int i = 0; i < dtoRecipes.size(); i++) {
			final Recipe dboRecipe = dboRecipes.get(i);
			final RecipeDto dtoRecipe = dtoRecipes.get(i);
			
			assertEquals(dboRecipe.getId(), dtoRecipe.getId());
			assertNotNull(dtoRecipe.getDishType());
			assertEquals(dboRecipe.getDishType().getId(), dtoRecipe.getDishType().getId());
			assertEquals(dboRecipe.getDishType().getName(), dtoRecipe.getDishType().getName());
			assertEquals(dboRecipe.getName(), dtoRecipe.getName());
			assertEquals(dboRecipe.getDescription(), dtoRecipe.getDescription());
			
			final List<RecipeIngredient> dboIngredients = dboRecipe.getRecipeIngredients();
			final List<IngredientDto> dtoIngredients = dtoRecipe.getIngredients();
			
			assertEquals(dboIngredients.size(), dtoIngredients.size());
			assertTrue(dtoIngredients.size() > 0);
			
			for (int j = 0; j < dtoIngredients.size(); j++) {
				final RecipeIngredient dboIngredient = dboIngredients.get(j);
				final IngredientDto dtoIngredient = dtoIngredients.get(j);
				
				assertEquals(dboIngredient.getId(), dtoIngredient.getId());
				assertEquals(dboIngredient.getIngredient().getId(), dtoIngredient.getIngredientNameId());
				assertEquals(dboIngredient.getIngredient().getName(), dtoIngredient.getName());
				assertEquals(dboIngredient.getCount(), dtoIngredient.getCount());
				assertNotNull(dtoIngredient.getMeasure());
				assertEquals(dboIngredient.getIngredientMeasure().getId(), dtoIngredient.getMeasure().getId());
				assertEquals(dboIngredient.getIngredientMeasure().getName(), dtoIngredient.getMeasure().getName());
			}
		}
	}
	
	@Test
	public void getRecipeById() {
		final Long id = 1L;
		final Optional<Recipe> optionalDboRecipe = MockData.optionalDboSauceRecipe(true);
		final Recipe dboRecipe = optionalDboRecipe.get();
		
		when(recipesDAO.findById(id)).thenReturn(optionalDboRecipe);
		
		final RecipeDto dtoRecipe = recipesService.getRecipe(id);
		
		assertEquals(dboRecipe.getId(), dtoRecipe.getId());
		assertNotNull(dtoRecipe.getDishType());
		assertEquals(dboRecipe.getDishType().getId(), dtoRecipe.getDishType().getId());
		assertEquals(dboRecipe.getDishType().getName(), dtoRecipe.getDishType().getName());
		assertEquals(dboRecipe.getName(), dtoRecipe.getName());
		assertEquals(dboRecipe.getDescription(), dtoRecipe.getDescription());
		
		final List<RecipeIngredient> dboIngredients = dboRecipe.getRecipeIngredients();
		final List<IngredientDto> dtoIngredients = dtoRecipe.getIngredients();
		
		assertEquals(dboIngredients.size(), dtoIngredients.size());
		assertTrue(dtoIngredients.size() > 0);
		
		for (int j = 0; j < dtoIngredients.size(); j++) {
			final RecipeIngredient dboIngredient = dboIngredients.get(j);
			final IngredientDto dtoIngredient = dtoIngredients.get(j);
			
			assertEquals(dboIngredient.getId(), dtoIngredient.getId());
			assertEquals(dboIngredient.getIngredient().getId(), dtoIngredient.getIngredientNameId());
			assertEquals(dboIngredient.getIngredient().getName(), dtoIngredient.getName());
			assertEquals(dboIngredient.getCount(), dtoIngredient.getCount());
			assertNotNull(dtoIngredient.getMeasure());
			assertEquals(dboIngredient.getIngredientMeasure().getId(), dtoIngredient.getMeasure().getId());
			assertEquals(dboIngredient.getIngredientMeasure().getName(), dtoIngredient.getMeasure().getName());
		}
	}
	
	@Test
	public void saveNewRecipe() {
		final Recipe dboRecipe = MockData.dboSauceRecipe(true);
		
		final Recipe dboNewRecipe = MockData.dboSauceRecipe(false);
		dboNewRecipe.getRecipeIngredients().get(0).setRecipe(dboNewRecipe);
		
		when(recipesDAO.saveAndFlush(dboNewRecipe)).thenReturn(dboRecipe);
		
		final RecipeDto dtoRecipe = recipesService.saveRecipe(MockData.dtoSauceRecipe(false));
		
		assertNotNull(dtoRecipe.getId());
		assertNotNull(dtoRecipe.getDishType());
		assertEquals(dboRecipe.getDishType().getId(), dtoRecipe.getDishType().getId());
		assertEquals(dboRecipe.getDishType().getName(), dtoRecipe.getDishType().getName());
		assertEquals(dboRecipe.getName(), dtoRecipe.getName());
		assertEquals(dboRecipe.getDescription(), dtoRecipe.getDescription());
		
		final List<RecipeIngredient> dboIngredients = dboRecipe.getRecipeIngredients();
		final List<IngredientDto> dtoIngredients = dtoRecipe.getIngredients();
		
		assertEquals(dboIngredients.size(), dtoIngredients.size());
		assertTrue(dtoIngredients.size() > 0);
		
		for (int j = 0; j < dtoIngredients.size(); j++) {
			final RecipeIngredient dboIngredient = dboIngredients.get(j);
			final IngredientDto dtoIngredient = dtoIngredients.get(j);
			
			assertEquals(dboIngredient.getId(), dtoIngredient.getId());
			assertEquals(dboIngredient.getIngredient().getId(), dtoIngredient.getIngredientNameId());
			assertEquals(dboIngredient.getIngredient().getName(), dtoIngredient.getName());
			assertEquals(dboIngredient.getCount(), dtoIngredient.getCount());
			assertNotNull(dtoIngredient.getMeasure());
			assertEquals(dboIngredient.getIngredientMeasure().getId(), dtoIngredient.getMeasure().getId());
			assertEquals(dboIngredient.getIngredientMeasure().getName(), dtoIngredient.getMeasure().getName());
		}
	}
	
	@Test
	public void updateRecipe() {
		final Recipe dboRecipe = MockData.dboSauceRecipe(true);
		dboRecipe.getRecipeIngredients().get(0).setRecipe(dboRecipe);
		
		when(recipesDAO.saveAndFlush(dboRecipe)).thenReturn(dboRecipe);
		
		final RecipeDto dtoRecipe = recipesService.saveRecipe(MockData.dtoSauceRecipe(true));
		
		assertNotNull(dtoRecipe.getId());
		assertNotNull(dtoRecipe.getDishType());
		assertEquals(dboRecipe.getDishType().getId(), dtoRecipe.getDishType().getId());
		assertEquals(dboRecipe.getDishType().getName(), dtoRecipe.getDishType().getName());
		assertEquals(dboRecipe.getName(), dtoRecipe.getName());
		assertEquals(dboRecipe.getDescription(), dtoRecipe.getDescription());
		
		final List<RecipeIngredient> dboIngredients = dboRecipe.getRecipeIngredients();
		final List<IngredientDto> dtoIngredients = dtoRecipe.getIngredients();
		
		assertEquals(dboIngredients.size(), dtoIngredients.size());
		assertTrue(dtoIngredients.size() > 0);
		
		for (int j = 0; j < dtoIngredients.size(); j++) {
			final RecipeIngredient dboIngredient = dboIngredients.get(j);
			final IngredientDto dtoIngredient = dtoIngredients.get(j);
			
			assertEquals(dboIngredient.getId(), dtoIngredient.getId());
			assertEquals(dboIngredient.getIngredient().getId(), dtoIngredient.getIngredientNameId());
			assertEquals(dboIngredient.getIngredient().getName(), dtoIngredient.getName());
			assertEquals(dboIngredient.getCount(), dtoIngredient.getCount());
			assertNotNull(dtoIngredient.getMeasure());
			assertEquals(dboIngredient.getIngredientMeasure().getId(), dtoIngredient.getMeasure().getId());
			assertEquals(dboIngredient.getIngredientMeasure().getName(), dtoIngredient.getMeasure().getName());
		}
	}
	
	@Test
	public void deleteRecipe() {
		final Long id = 1L;
		
		final RecipeDto dtoRecipe = recipesService.deleteRecipe(id);
		
		assertEquals(id, dtoRecipe.getId());
		assertNull(dtoRecipe.getDishType());
		assertNull(dtoRecipe.getName());
		assertNull(dtoRecipe.getDescription());
		assertNull(dtoRecipe.getIngredients());
	}
	
	@Test
	public void validateCorrectRecipe() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(true);
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNull(validationResult);
	}
	
	@Test
	public void validateCorrectNewRecipe() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(false);
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNull(validationResult);
	}
	
	@Test
	public void validateRecipeWithoutName() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(true);
		dtoRecipe.setName(null);
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof RecipeError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void validateRecipeWithoutDishType() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(true);
		dtoRecipe.setDishType(null);
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof RecipeError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void validateRecipeWithoutDishTypeId() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(true);
		dtoRecipe.getDishType().setId(null);
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof RecipeError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void validateNewRecipeWithExistingName() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(false);
		
		when(recipesDAO.findAllByNameAndDishType(dtoRecipe.getName(), dtoRecipe.getDishType().getId())).thenReturn(MockData.listDboRecipes());
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof RecipeError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void validateRecipeWithoutIngredients() {
		final RecipeDto dtoRecipe = MockData.dtoSauceRecipe(true);
		dtoRecipe.setIngredients(Collections.<IngredientDto>emptyList());
		
		ResponseEntity<RecipeDto> validationResult = recipesService.validateRecipe(dtoRecipe);
		
		assertNotNull(validationResult);
		assertTrue(validationResult.getBody() instanceof RecipeError);
		assertEquals(validationResult.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
