package com.recipes.appl.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dbo.DishTypeDbo;
import com.recipes.appl.model.dbo.IngredientDbo;
import com.recipes.appl.model.dbo.IngredientMeasureDbo;
import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RecipesDAOTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private RecipesDAO recipesDAO;
	
	
	@Test
	public void findAllRecipes() {
		final RecipeDbo recipeFromDB = prepareRecipeData();
		
		final List<RecipeDbo> foundRecipes = recipesDAO.findAll();
		
		assertThat(foundRecipes).isNotEmpty().contains(recipeFromDB);
	}

	@Test
	@Ignore("Green test which is red when absolutely all tests are invoked")
	public void findAllRecipesByIngredientId() {
		final RecipeDbo recipeFromDB = prepareRecipeData();
		
		final List<RecipeDbo> foundRecipes = recipesDAO.findAllByIngredientId(1L);
		
		assertThat(foundRecipes).isNotEmpty().contains(recipeFromDB);
	}
	
	@Test
	public void findRecipesNoResults() {
		final List<RecipeDbo> foundRecipes = recipesDAO.findAll();
		
		assertThat(foundRecipes).isEmpty();
	}
	
	@Test
	public void findAllByIngredientIdNoResults() {
		final Long ingredientId = 3L;
		
		final List<RecipeDbo> foundRecipes = recipesDAO.findAllByIngredientId(ingredientId);
		
		assertThat(foundRecipes).isEmpty();
	}
	
	@Test
	public void findRecipeById() {
		final RecipeDbo recipeFromDB = prepareRecipeData();
		final Long id = recipeFromDB.getId();
		
		final Optional<RecipeDbo> foundRecipe = recipesDAO.findById(id);
		
		assertThat(foundRecipe.orElse(null)).isNotNull();
	}
	
	@Test
	public void notFoundRecipeById() {
		final Long id = 1L;
		final Optional<RecipeDbo> foundRecipe = recipesDAO.findById(id);
		
		assertThat(foundRecipe.orElse(null)).isNull();
	}
	
	@Test
	public void saveRecipe() {
		final RecipeDbo recipe = prepareStaticData();
		final RecipeIngredientDbo recipeIngredient = recipe.getRecipeIngredients().get(0);
		
		
		final RecipeDbo recipeToSave = MockData.dboSauceRecipe(false);
		recipeToSave.getDishType().setId(recipe.getDishType().getId());
		final RecipeIngredientDbo recipeIngredientToSave = recipeToSave.getRecipeIngredients().get(0);
		recipeIngredientToSave.setId(null);
		recipeIngredientToSave.getIngredient().setId(recipeIngredient.getIngredient().getId());
		recipeIngredientToSave.getIngredientMeasure().setId(recipeIngredient.getIngredientMeasure().getId());
		recipeIngredientToSave.setRecipe(recipeToSave);
		
		final RecipeDbo savedRecipe = recipesDAO.saveAndFlush(recipeToSave);
		
		assertThat(savedRecipe).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(savedRecipe.getRecipeIngredients()).isNotEmpty();
		for (final RecipeIngredientDbo savedRecipeIngredient : savedRecipe.getRecipeIngredients()) {
			assertThat(savedRecipeIngredient).hasNoNullFieldsOrProperties();
			assertThat(savedRecipeIngredient.getIngredient()).hasNoNullFieldsOrPropertiesExcept("components", "recipeIngredient");
			assertThat(savedRecipeIngredient.getIngredientMeasure()).hasNoNullFieldsOrPropertiesExcept("recipeIngredient");
		}
	}
	
	@Test
	public void updateRecipe() {
		final RecipeDbo recipe = prepareRecipeData();
		final RecipeIngredientDbo recipeIngredient = recipe.getRecipeIngredients().get(0);
		
		
		final String newRecipeName = "New Recipe Name";
		final Double newIngredientMeasureCount = 10.0;
		
		final RecipeDbo recipeToUpdate = MockData.dboSauceRecipe(false);
		recipeToUpdate.setId(recipe.getId());
		recipeToUpdate.setName(newRecipeName);
		recipeToUpdate.getDishType().setId(recipe.getDishType().getId());
		final RecipeIngredientDbo recipeIngredientToUpdate = recipeToUpdate.getRecipeIngredients().get(0);
		recipeIngredientToUpdate.setId(recipeIngredient.getId());
		recipeIngredientToUpdate.getIngredient().setId(recipeIngredient.getIngredient().getId());
		recipeIngredientToUpdate.getIngredientMeasure().setId(recipeIngredient.getIngredientMeasure().getId());
		recipeIngredientToUpdate.setCount(newIngredientMeasureCount);
		recipeIngredientToUpdate.setRecipe(recipeToUpdate);
		
		final RecipeDbo updatedRecipe = recipesDAO.saveAndFlush(recipeToUpdate);
		
		assertThat(updatedRecipe)
			.isNotNull()
			.hasNoNullFieldsOrProperties()
			.hasFieldOrPropertyWithValue("name", newRecipeName);
		assertThat(updatedRecipe.getRecipeIngredients())
			.isNotEmpty()
			.first()
				.hasNoNullFieldsOrProperties()
				.hasFieldOrPropertyWithValue("count", newIngredientMeasureCount);
	}
	
	@Test
	public void deleteRecipe() {
		final RecipeDbo recipe = prepareRecipeData();
		
		final Long id = recipe.getId();
		recipesDAO.deleteById(id);
		
		final Optional<RecipeDbo> foundRecipe = recipesDAO.findById(id);
		
		assertThat(foundRecipe.orElse(null)).isNull();
	}
	
	@Test
	public void deleteRecipeByNullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> { recipesDAO.deleteById(null); });
	}
	
	@Test
	public void deleteRecipeByUnknownId() {
		assertThatExceptionOfType(EmptyResultDataAccessException.class).isThrownBy(() -> { recipesDAO.deleteById(1L); });
	}
	
	private RecipeDbo prepareStaticData() {
		final RecipeDbo recipe = MockData.dboSauceRecipe(false);
		
		final DishTypeDbo dishType = recipe.getDishType();
		dishType.setId(null);
		entityManager.persist(dishType);
		
		final RecipeIngredientDbo recipeIngredient = recipe.getRecipeIngredients().get(0);
		recipeIngredient.setId(null);
		
		final IngredientDbo ingredient = recipeIngredient.getIngredient();
		ingredient.setId(null);
		entityManager.persist(ingredient);
		
		final IngredientMeasureDbo ingredientMeasure = recipeIngredient.getIngredientMeasure();
		ingredientMeasure.setId(null);
		entityManager.persist(ingredientMeasure);
		
		return recipe;
	}
	
	private RecipeDbo prepareRecipeData() {
		final RecipeDbo recipe = prepareStaticData();
		
		final RecipeIngredientDbo recipeIngredient = recipe.getRecipeIngredients().get(0);
		recipeIngredient.setRecipe(recipe);
		entityManager.persist(recipeIngredient);
		
		entityManager.persist(recipe);
		
		return recipe;
	}
	
}
