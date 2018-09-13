package com.recipes.appl.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.service.RecipesService;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UsersRecipesController.class)
public class UsersRecipesControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RecipesService recipesService;
	
	
	@Test
	@WithMockUser
	public void getRecipesPage() throws Exception {
		
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByName() throws Exception {
		final String recipeName = "Recipe";
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		resultList.add(recipe);
		
		final Map<String, Object> params = new HashMap<>();
		params.put("name", recipeName);
		
		when(recipesService.searchRecipes(params)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipeName).with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(2L))),
					hasProperty("dishType", hasProperty("name", is("Соус"))),
					hasProperty("name", is(recipeName)),
					hasProperty("description", is("Description")),
					hasProperty("ingredients", hasItem(
						allOf(
							hasProperty("id", is(2L)),
							hasProperty("ingredientNameId", is(1L)),
							hasProperty("name", is("Кукуруза")),
							hasProperty("count", is(2.0)),
							hasProperty("measure", hasProperty("id", is(1L))),
							hasProperty("measure", hasProperty("name", is("мл")))
						)
					))
				))))
			.andExpect(view().name("recipes"));
	}
	
	/*@Test
	@WithMockUser
	public void searchRecipesByDishType() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?dishtype=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishType() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?name=&dishtype=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByComponents() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByIngredients() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?ingredients=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndIngredients() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?name=&dishtype=&ingredients=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndComponents() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?name=&dishtype=&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndIngredientsAndComponents() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?name=&dishtype=&ingredients=&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByDishTypeAndIngredients() throws Exception {
		// TODO
		when(recipesService.getRecipes()).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes?dishtype=&ingredients=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes"))
			.andExpect(view().name("recipes"));
	}*/
	
	@Test
	@WithMockUser
	public void getSingleRecipePage() throws Exception {
		final Long recipeId = 1L;
		
		when(recipesService.getRecipe(recipeId)).thenReturn(MockData.dtoSauceRecipe(true));
		
		mvc.perform(get("/recipe").param("id", recipeId.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipe"))
			.andExpect(view().name("singlerecipe"));
	}
	
	@Test
	public void getAdminLoginPage() throws Exception {
		mvc.perform(get("/login").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void getRecipesIncorrectHttpMethod() throws Exception {
		mvc.perform(post("/recipes").with(csrf()))
			.andDo(print())
			.andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
	}
	
}
