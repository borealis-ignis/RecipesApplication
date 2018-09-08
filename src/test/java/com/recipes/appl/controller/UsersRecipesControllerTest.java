package com.recipes.appl.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
