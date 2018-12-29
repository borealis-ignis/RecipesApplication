package com.recipes.appl.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.recipes.appl.model.dto.ComponentDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.service.IngredientsService;
import com.recipes.appl.service.RecipesService;
import com.recipes.appl.util.StringUtil;

/**
 * @author Kastalski Sergey
 */
@Controller
public class UsersRecipesController {
	
	private RecipesService recipesService;
	
	private IngredientsService ingredientsService;
	
	private final static int ITEMS_PER_PAGE = 10;
	
	private final static int FIRST_PAGE = 0;
	
	
	@Autowired
	public UsersRecipesController(final RecipesService recipesService, final IngredientsService ingredientsService) {
		this.recipesService = recipesService;
		this.ingredientsService = ingredientsService;
	}
	
	
	@GetMapping(path="/recipes")
	public String getRecipesPage(final Map<String, Object> model,
			@RequestParam(name="name", required=false) final String namePart,
			@RequestParam(name="dishtype", required=false) final Long dishTypeId,
			@RequestParam(name="ingredients", required=false) final String ingredientsIds,
			@RequestParam(name="components", required=false) final String componentsIds,
			@RequestParam(name="page", required=false) final Integer pageNumber) {
		
		final String delimiter = ";";
		final List<Long> ingredientsIdsList = StringUtil.toList(ingredientsIds, delimiter);
		final List<Long> componentsIdsList = StringUtil.toList(componentsIds, delimiter);
		
		final List<IngredientDto> ingredientsList = ingredientsService.getIngredients();
		final Set<ComponentDto> componentsSet = new LinkedHashSet<>();
		ingredientsList.forEach(
				ingredient -> ingredient.getComponents().forEach(
						component -> componentsSet.add(component)
				)
		);
		
		final int currentPage = (pageNumber == null || pageNumber < FIRST_PAGE)? FIRST_PAGE : pageNumber;
		final Pageable pageable = PageRequest.of(currentPage, ITEMS_PER_PAGE);
		final long recipesCount = recipesService.recipesCount(namePart, dishTypeId, ingredientsIdsList, componentsIdsList);
		final long recipesPages = (long) Math.ceil((double) recipesCount / (double) ITEMS_PER_PAGE);
		
		model.put("recipes", recipesService.searchRecipes(namePart, dishTypeId, ingredientsIdsList, componentsIdsList, pageable));
		model.put("recipesPages", recipesPages);
		model.put("currentPage", currentPage);
		model.put("dishTypes", recipesService.getDishTypes());
		model.put("ingredients", ingredientsList);
		model.put("components", componentsSet);
		
		model.put("enteredNamePart", namePart);
		model.put("selectedDishTypeId", dishTypeId);
		model.put("selectedIngredientsIds", ingredientsIdsList);
		model.put("selectedComponentsIds", componentsIdsList);
		
		return "recipes";
	}
	
	@GetMapping(path="/recipe")
	public String getSingleRecipePage(final Map<String, Object> model,
			@RequestParam(required = true, name = "id") final Long id) {
		model.put("recipe", recipesService.getRecipe(id));
		return "singlerecipe";
	}
	
	@GetMapping(path="/login")
	public String getAdminLoginPage(final Map<String, Object> model) {
		return "login";
	}
	
	@GetMapping(path="/403")
	public String error403(final Map<String, Object> model) {
		return "error/403";
	}
}
