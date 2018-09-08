package com.recipes.appl.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
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

import org.hamcrest.core.IsNull;
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
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.errors.IngredientError;
import com.recipes.appl.service.IngredientsService;
import com.recipes.appl.utils.ConvertersUtil;

import net.minidev.json.JSONArray;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@WebMvcTest(IngredientsAdminController.class)
public class IngredientsAdminControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private IngredientsService ingredientsService;
	
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getAdminIngredientsPageTest() throws Exception {
		
		when(ingredientsService.getIngredients()).thenReturn(MockData.listDtoIngredients(false));
		when(ingredientsService.getComponents()).thenReturn(MockData.listDtoComponents());
		
		mvc.perform(get("/admin/ingredients"))
			.andDo(print())
			.andExpect(status().isOk())
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
			.andExpect(model().attribute("components", hasItem(
				allOf(
					hasProperty("id", is(1L)),
					hasProperty("name", is("Кальций"))
				))))
			.andExpect(model().attribute("components", hasItem(
				allOf(
					hasProperty("id", is(2L)),
					hasProperty("name", is("Магний"))
				))))
			.andExpect(model().size(2))
			.andExpect(view().name("admin/ingredients"));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getComponentsByIngredientId() throws Exception {
		final Long ingredientId = 1L;
		
		when(ingredientsService.getComponents(ingredientId)).thenReturn(MockData.listDtoComponents());
		
		mvc.perform(get("/admin/ingredient/components").param("id", ingredientId.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", isA(JSONArray.class)))
			.andExpect(jsonPath("$.length()", is(2)))
			.andExpect(jsonPath("$[0].name", is(equalTo("Кальций"))))
			.andExpect(jsonPath("$[1].name", is(equalTo("Магний"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getAllComponents() throws Exception {
		
		when(ingredientsService.getComponents()).thenReturn(MockData.listDtoComponents());
		
		mvc.perform(get("/admin/ingredient/allcomponents"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", isA(JSONArray.class)))
			.andExpect(jsonPath("$.length()", is(2)))
			.andExpect(jsonPath("$[0].name", is(equalTo("Кальций"))))
			.andExpect(jsonPath("$[1].name", is(equalTo("Магний"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void saveNewIngredientWithoutComponents() throws Exception {
		final IngredientDto newIngredient = MockData.dtoAlmondIngredient(false, false);
		when(ingredientsService.saveIngredient(newIngredient)).thenReturn(MockData.dtoAlmondIngredient(false, true));
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newIngredient)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Миндаль")))
			.andExpect(jsonPath("$.components").value(IsNull.nullValue()));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void saveNewIngredientWithComponents() throws Exception {
		final IngredientDto newIngredient = MockData.dtoAlmondIngredient(true, false);
		when(ingredientsService.saveIngredient(newIngredient)).thenReturn(MockData.dtoAlmondIngredient(true, true));
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newIngredient)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Миндаль")))
			.andExpect(jsonPath("$.components", isA(JSONArray.class)))
			.andExpect(jsonPath("$.components.length()", is(2)))
			.andExpect(jsonPath("$.components[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$.components[0].name", is(equalTo("Кальций"))))
			.andExpect(jsonPath("$.components[1].id", is(equalTo(2))))
			.andExpect(jsonPath("$.components[1].name", is(equalTo("Магний"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void saveNewIngredientBadRequest() throws Exception {
		final IngredientDto newIngredient = MockData.dtoAlmondIngredient(false, false);
		when(ingredientsService.validateIngredient(newIngredient)).thenReturn(new ResponseEntity<IngredientDto>(new IngredientError("Validation error message"), HttpStatus.BAD_REQUEST));
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(newIngredient)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.errorMessage", is("Validation error message")));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void updateIngredientWithoutComponents() throws Exception {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, true);
		when(ingredientsService.saveIngredient(ingredient)).thenReturn(ingredient);
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(ingredient)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Миндаль")))
			.andExpect(jsonPath("$.components").value(IsNull.nullValue()));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void updateIngredientWithComponents() throws Exception {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(true, true);
		when(ingredientsService.saveIngredient(ingredient)).thenReturn(ingredient);
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(ingredient)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Миндаль")))
			.andExpect(jsonPath("$.components", isA(JSONArray.class)))
			.andExpect(jsonPath("$.components.length()", is(2)))
			.andExpect(jsonPath("$.components[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$.components[0].name", is(equalTo("Кальций"))))
			.andExpect(jsonPath("$.components[1].id", is(equalTo(2))))
			.andExpect(jsonPath("$.components[1].name", is(equalTo("Магний"))));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void updateIngredientBadRequest() throws Exception {
		final IngredientDto ingredient = MockData.dtoAlmondIngredient(false, true);
		when(ingredientsService.validateIngredient(ingredient)).thenReturn(new ResponseEntity<IngredientDto>(new IngredientError("Validation error message"), HttpStatus.BAD_REQUEST));
		
		mvc.perform(post("/admin/ingredient/save")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(ConvertersUtil.json(ingredient)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.errorMessage", is("Validation error message")));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void deleteIngredient() throws Exception {
		final Long ingredientId = 1L;
		final IngredientDto dto = new IngredientDto();
		dto.setId(ingredientId);
		
		when(ingredientsService.deleteIngredient(ingredientId)).thenReturn(dto);
		
		mvc.perform(delete("/admin/ingredient/delete").param("id", ingredientId.toString()).with(csrf()))
			.andDo(print())
			.andExpect(status().isAccepted())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.id", is(1)));
	}
	
	@Test
	@WithMockUser(username="admin", password="admin", roles="ADMIN")
	public void getAllComponentsIncorrectHttpMethod() throws Exception {
		mvc.perform(post("/admin/ingredient/allcomponents").with(csrf()))
			.andDo(print())
			.andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
	}
	
	@Test
	public void getAllComponentsNotAuthorized() throws Exception {
		mvc.perform(get("/admin/ingredient/allcomponents"))
			.andDo(print())
			.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
	}
	
}
