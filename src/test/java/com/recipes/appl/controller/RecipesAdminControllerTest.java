package com.recipes.appl.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.service.IngredientsService;
import com.recipes.appl.service.RecipesService;
import com.recipes.appl.utils.ConvertersUtil;

import net.minidev.json.JSONArray;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@WebMvcTest(RecipesAdminController.class)
public class RecipesAdminControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private IngredientsService ingredientsService;
	
	@MockBean
	private RecipesService recipesService;
	
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getAdminRecipesPage() throws Exception {
		
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.getIngredientMeasures()).thenReturn(MockData.listDtoIngredientMeasures());
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(false));
		
		mvc.perform(get("/admin/recipes"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("dishTypes", hasItem(
				allOf(
					hasProperty("id", is(1L)),
					hasProperty("name", is("Суп"))
				))))
			.andExpect(model().attribute("dishTypes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("name", is("Соус"))
				))))
			.andExpect(model().attribute("ingredientMeasures", hasItem(
				allOf(
					hasProperty("id", is(1L)),
					hasProperty("name", is("мл"))
				))))
			.andExpect(model().attribute("ingredientMeasures", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("name", is("чайных ложек"))
				))))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(1L)),
					hasProperty("name", is("Recipe#1"))
				))))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("name", is("Recipe#2"))
				))))
			.andExpect(model().attribute("ingredients", hasItem(
				allOf(
					hasProperty("id", is(1L)),
					hasProperty("name", is("Миндаль"))
				))))
			.andExpect(model().attribute("ingredients", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("name", is("Кукуруза"))
				))))
			.andExpect(model().size(4))
			.andExpect(view().name("admin/recipes"));
		
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getRecipe() throws Exception {
		final Long recipeId = 1L;
		
		when(recipesService.getRecipe(recipeId)).thenReturn(MockData.dtoSauceRecipe(true));
		
		mvc.perform(get("/admin/recipe").param("id", recipeId.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(equalTo(1))))
			.andExpect(jsonPath("$.name", is(equalTo("Recipe#1"))))
			.andExpect(jsonPath("$.dishType.id", is(equalTo(2))))
			.andExpect(jsonPath("$.dishType.name", is(equalTo("Соус"))))
			.andExpect(jsonPath("$.description", is(equalTo("Description"))))
			.andExpect(jsonPath("$.ingredients", isA(JSONArray.class)))
			.andExpect(jsonPath("$.ingredients.length()", is(1)))
			.andExpect(jsonPath("$.ingredients[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].ingredientNameId", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].name", is(equalTo("Миндаль"))))
			.andExpect(jsonPath("$.ingredients[0].count", is(equalTo(2.0))))
			.andExpect(jsonPath("$.ingredients[0].measure.id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].measure.name", is(equalTo("мл"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void saveNewRecipe() throws Exception {
		final RecipeDto newRecipe = MockData.dtoSauceRecipe(false);
		when(recipesService.saveRecipe(newRecipe)).thenReturn(MockData.dtoSauceRecipe(true));
		
		mvc.perform(post("/admin/recipe/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newRecipe)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(equalTo(1))))
			.andExpect(jsonPath("$.name", is(equalTo("Recipe#1"))))
			.andExpect(jsonPath("$.dishType.id", is(equalTo(2))))
			.andExpect(jsonPath("$.dishType.name", is(equalTo("Соус"))))
			.andExpect(jsonPath("$.description", is(equalTo("Description"))))
			.andExpect(jsonPath("$.ingredients", isA(JSONArray.class)))
			.andExpect(jsonPath("$.ingredients.length()", is(1)))
			.andExpect(jsonPath("$.ingredients[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].ingredientNameId", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].name", is(equalTo("Миндаль"))))
			.andExpect(jsonPath("$.ingredients[0].count", is(equalTo(2.0))))
			.andExpect(jsonPath("$.ingredients[0].measure.id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].measure.name", is(equalTo("мл"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void saveNewRecipeBadRequest() throws Exception {
		final RecipeDto newRecipe = MockData.dtoSauceRecipe(false);
		when(recipesService.validateRecipe(newRecipe)).thenReturn(new ResponseEntity<RecipeDto>(new RecipeError("Validation error message"), HttpStatus.BAD_REQUEST));
		
		mvc.perform(post("/admin/recipe/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newRecipe)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.errorMessage", is("Validation error message")));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void updateRecipe() throws Exception {
		final RecipeDto recipe = MockData.dtoSauceRecipe(true);
		when(recipesService.saveRecipe(recipe)).thenReturn(recipe);
		
		mvc.perform(post("/admin/recipe/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(recipe)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(equalTo(1))))
			.andExpect(jsonPath("$.name", is(equalTo("Recipe#1"))))
			.andExpect(jsonPath("$.dishType.id", is(equalTo(2))))
			.andExpect(jsonPath("$.dishType.name", is(equalTo("Соус"))))
			.andExpect(jsonPath("$.description", is(equalTo("Description"))))
			.andExpect(jsonPath("$.ingredients", isA(JSONArray.class)))
			.andExpect(jsonPath("$.ingredients.length()", is(1)))
			.andExpect(jsonPath("$.ingredients[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].ingredientNameId", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].name", is(equalTo("Миндаль"))))
			.andExpect(jsonPath("$.ingredients[0].count", is(equalTo(2.0))))
			.andExpect(jsonPath("$.ingredients[0].measure.id", is(equalTo(1))))
			.andExpect(jsonPath("$.ingredients[0].measure.name", is(equalTo("мл"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void updateRecipeBadRequest() throws Exception {
		final RecipeDto newRecipe = MockData.dtoSauceRecipe(true);
		when(recipesService.validateRecipe(newRecipe)).thenReturn(new ResponseEntity<RecipeDto>(new RecipeError("Validation error message"), HttpStatus.BAD_REQUEST));
		
		mvc.perform(post("/admin/recipe/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newRecipe)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.errorMessage", is("Validation error message")));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void deleteRecipe() throws Exception {
		final Long recipeId = 1L;
		final RecipeDto dto = new RecipeDto();
		dto.setId(recipeId);
		
		when(recipesService.deleteRecipe(recipeId)).thenReturn(dto);
		
		mvc.perform(delete("/admin/recipe/delete").param("id", recipeId.toString()).with(csrf()))
			.andDo(print())
			.andExpect(status().isAccepted())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getRecipeIncorrectHttpMethod() throws Exception {
		mvc.perform(post("/admin/recipe").param("id", "1").with(csrf()))
			.andDo(print())
			.andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
	}
	
	@Test
	public void getRecipeNotAuthorized() throws Exception {
		mvc.perform(get("/admin/recipe").param("id", "1").with(csrf()))
			.andDo(print())
			.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
}
