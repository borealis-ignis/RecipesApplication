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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.service.IngredientsService;
import com.recipes.appl.service.RecipesService;
import com.recipes.appl.util.StringUtil;

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
	
	@MockBean
	private IngredientsService ingredientsService;
	
	private static int ITEMS_PER_PAGE = 0;
	{
		final Field field;
		try {
			field = UsersRecipesController.class.getDeclaredField("ITEMS_PER_PAGE");
			field.setAccessible(true);
			ITEMS_PER_PAGE = field.getInt(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@WithMockUser
	public void getRecipesPage() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(null, null, StringUtil.toList("", ";"), StringUtil.toList("", ";"), pageable)).thenReturn(MockData.listDtoRecipes());
		
		mvc.perform(get("/recipes").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", IsNull.nullValue()))
			.andExpect(model().attribute("selectedDishTypeId", IsNull.nullValue()))
			.andExpect(model().attribute("selectedIngredientsIds", IsEmptyCollection.empty()))
			.andExpect(model().attribute("selectedComponentsIds", IsEmptyCollection.empty()))
			.andExpect(model().attribute("recipes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(view().name("recipes"));
	}
	
	@Test
	@WithMockUser
	public void searchRecipesByName() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String recipeName = "Recipe";
		final String recipePartName = "eci";
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(recipePartName, null, StringUtil.toList("", ";"), StringUtil.toList("", ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipePartName + "&dishtype=&ingredients=&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is(recipePartName)))
			.andExpect(model().attribute("selectedDishTypeId", IsNull.nullValue()))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
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
	
	@Test
	@WithMockUser
	public void searchRecipesByDishType() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final Long dishTypeId = 10L;
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.getDishType().setId(dishTypeId);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes("", dishTypeId, StringUtil.toList("", ";"), StringUtil.toList("", ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=&dishtype=" + dishTypeId + "&ingredients=&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedDishTypeId", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("selectedDishTypeId", is(dishTypeId)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(dishTypeId))),
					hasProperty("dishType", hasProperty("name", is("Соус"))),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishType() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String recipeName = "Recipe";
		final String recipePartName = "eci";
		final Long dishTypeId = 10L;
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		recipe.getDishType().setId(dishTypeId);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(recipePartName, dishTypeId, StringUtil.toList("", ";"), StringUtil.toList("", ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipePartName + "&dishtype=" + dishTypeId + "&ingredients=&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedDishTypeId", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is(recipePartName)))
			.andExpect(model().attribute("selectedDishTypeId", is(dishTypeId)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(dishTypeId))),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByComponents() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String componentsString = "10;2;";
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes("", null, StringUtil.toList("", ";"), StringUtil.toList(componentsString, ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=&dishtype=&ingredients=&components=" + componentsString).with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is("")))
			.andExpect(model().attribute("selectedDishTypeId", IsNull.nullValue()))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedComponentsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(2L))),
					hasProperty("dishType", hasProperty("name", is("Соус"))),
					hasProperty("name", is("Recipe#2")),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByIngredients() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String ingredientsString = "2;4;";
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes("", null, StringUtil.toList(ingredientsString, ";"), StringUtil.toList("", ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=&dishtype=&ingredients=" + ingredientsString + "&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is("")))
			.andExpect(model().attribute("selectedDishTypeId", IsNull.nullValue()))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedIngredientsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(2L))),
					hasProperty("dishType", hasProperty("name", is("Соус"))),
					hasProperty("name", is("Recipe#2")),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndIngredients() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String ingredientsString = "2;4;";
		final String recipeName = "Recipe";
		final String recipePartName = "eci";
		final Long dishTypeId = 10L;
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		recipe.getDishType().setId(dishTypeId);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(recipePartName, dishTypeId, StringUtil.toList(ingredientsString, ";"), StringUtil.toList("", ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipePartName + "&dishtype=" + dishTypeId + "&ingredients=" + ingredientsString + "&components=").with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedDishTypeId", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is(recipePartName)))
			.andExpect(model().attribute("selectedDishTypeId", is(dishTypeId)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedIngredientsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(dishTypeId))),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndComponents() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String componentsString = "10;2;";
		final String recipeName = "Recipe";
		final String recipePartName = "eci";
		final Long dishTypeId = 10L;
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		recipe.getDishType().setId(dishTypeId);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(recipePartName, dishTypeId, StringUtil.toList("", ";"), StringUtil.toList(componentsString, ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipePartName + "&dishtype=" + dishTypeId + "&ingredients=&components=" + componentsString).with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedDishTypeId", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is(recipePartName)))
			.andExpect(model().attribute("selectedDishTypeId", is(dishTypeId)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedComponentsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(dishTypeId))),
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
	
	@Test
	@WithMockUser
	public void searchRecipesByNameAndDishTypeAndIngredientsAndComponents() throws Exception {
		final Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);
		
		final String ingredientsString = "2;4;";
		final String componentsString = "10;2;";
		final String recipeName = "Recipe";
		final String recipePartName = "eci";
		final Long dishTypeId = 10L;
		final List<RecipeDto> resultList = new ArrayList<>();
		final RecipeDto recipe = MockData.dtoSoupRecipe(true);
		recipe.setName(recipeName);
		recipe.getDishType().setId(dishTypeId);
		resultList.add(recipe);
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(true));
		when(recipesService.getDishTypes()).thenReturn(MockData.listDtoDishTypes());
		when(recipesService.searchRecipes(recipePartName, dishTypeId, StringUtil.toList(ingredientsString, ";"), StringUtil.toList(componentsString, ";"), pageable)).thenReturn(resultList);
		
		mvc.perform(get("/recipes?name=" + recipePartName + "&dishtype=" + dishTypeId + "&ingredients=" + ingredientsString + "&components=" + componentsString).with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipes", "dishTypes", "ingredients", "components", "enteredNamePart", "selectedDishTypeId", "selectedIngredientsIds", "selectedComponentsIds"))
			.andExpect(model().attribute("enteredNamePart", is(recipePartName)))
			.andExpect(model().attribute("selectedDishTypeId", is(dishTypeId)))
			.andExpect(model().attribute("dishTypes", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("ingredients", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("components", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedIngredientsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("selectedComponentsIds", IsCollectionWithSize.hasSize(2)))
			.andExpect(model().attribute("recipes", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("dishType", hasProperty("id", is(dishTypeId))),
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
