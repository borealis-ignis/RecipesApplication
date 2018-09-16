package com.recipes.appl.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Optional;

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
import com.recipes.appl.model.dbo.IngredientDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class IngredientsDAOTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private IngredientsDAO ingredientsDAO;
	
	
	@Test
	public void selectIngredients() {
		final IngredientDbo ingredient = prepareIngredientData();
		
		final List<IngredientDbo> foundIngredients = ingredientsDAO.selectIngredients();
		
		assertThat(foundIngredients).isNotEmpty().contains(ingredient);
	}
	
	@Test
	public void selectIngredientsNoResults() {
		final List<IngredientDbo> foundIngredients = ingredientsDAO.selectIngredients();
		
		assertThat(foundIngredients).isEmpty();
	}
	
	@Test
	public void findIngredientById() {
		final IngredientDbo ingredient = prepareIngredientData();
		
		final Long id = ingredient.getId();
		final Optional<IngredientDbo> foundIngredient = ingredientsDAO.findById(id);
		
		assertThat(foundIngredient.orElse(null)).isNotNull();
	}
	
	@Test
	public void notFoundIngredientById() {
		final Long id = 1L;
		final Optional<IngredientDbo> foundIngredient = ingredientsDAO.findById(id);
		
		assertThat(foundIngredient.orElse(null)).isNull();
	}
	
	@Test
	public void saveIngredient() {
		final IngredientDbo savedIngredient = ingredientsDAO.saveAndFlush(MockData.dboAlmondIngredientWithComponentsWithoutIds());
		
		assertThat(savedIngredient).isNotNull().hasNoNullFieldsOrPropertiesExcept("recipeIngredient");
	}
	
	@Test
	public void updateIngredient() {
		final IngredientDbo ingredientFromDB = prepareIngredientData();
		
		final String newIngredientName = "Almond";
		
		final IngredientDbo ingredient = MockData.dboAlmondIngredientWithComponentsWithoutIds();
		ingredient.setId(ingredientFromDB.getId());
		ingredient.setComponents(null);
		ingredient.setName(newIngredientName);
		
		final IngredientDbo updatedIngredient = ingredientsDAO.saveAndFlush(ingredient);
		
		assertThat(updatedIngredient)
			.isNotNull()
			.hasNoNullFieldsOrPropertiesExcept("components", "recipeIngredient")
			.hasFieldOrPropertyWithValue("name", newIngredientName);
	}
	
	@Test
	public void deleteIngredientById() {
		final IngredientDbo ingredient = prepareIngredientData();
		
		final Long id = ingredient.getId();
		ingredientsDAO.deleteById(id);
		
		final Optional<IngredientDbo> foundIngredient = ingredientsDAO.findById(id);
		
		assertThat(foundIngredient.orElse(null)).isNull();
	}
	
	@Test
	public void deleteIngredientByNullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> { ingredientsDAO.deleteById(null); });
	}
	
	@Test
	public void deleteIngredientByUnknownId() {
		assertThatExceptionOfType(EmptyResultDataAccessException.class).isThrownBy(() -> { ingredientsDAO.deleteById(1L); });
	}
	
	@Test
	public void findAllIngredientsByName() {
		final IngredientDbo ingredient = prepareIngredientData();
		
		final String ingredientName = ingredient.getName();
		final List<IngredientDbo> foundIngredients = ingredientsDAO.findAllByName(ingredientName);
		
		assertThat(foundIngredients).isNotEmpty().contains(ingredient);
	}
	
	@Test
	public void notFoundIngredientByName() {
		final String ingredientName = "Almond";
		final List<IngredientDbo> foundIngredients = ingredientsDAO.findAllByName(ingredientName);
		
		assertThat(foundIngredients).isEmpty();
	}
	
	
	private IngredientDbo prepareIngredientData() {
		final IngredientDbo ingredient = MockData.dboAlmondIngredientWithComponentsWithoutIds();
		entityManager.persist(ingredient);
		return ingredient;
	}
	
}
