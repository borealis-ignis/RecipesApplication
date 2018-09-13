package com.recipes.appl.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.recipes.appl.service.RecipesService;

/**
 * @author Kastalski Sergey
 */
@Controller
public class UsersRecipesController {
	
	@Autowired
	private RecipesService recipesService;
	
	
	@GetMapping(path="/recipes")
	public String getRecipesPage(final Map<String, Object> model,
			@RequestParam(name="name", required=false) String name,
			@RequestParam(name="dishtype", required=false) Long dishTypeId,
			@RequestParam(name="ingredients", required=false) String ingredientsIds,
			@RequestParam(name="components", required=false) String componentsIds) {
		// TODO
		final Map<String, Object> params = new HashMap<>();
		if (!StringUtils.isEmpty(name)) {
			params.put("name", name);
		}
		if (dishTypeId != null) {
			params.put("dishtype", dishTypeId);
		}
		if (!StringUtils.isEmpty(ingredientsIds)) {
			params.put("ingredients", ingredientsIds);
		}
		if (!StringUtils.isEmpty(componentsIds)) {
			params.put("components", componentsIds);
		}
		
		model.put("recipes", recipesService.searchRecipes(params));
		
		return "recipes";
	}
	
	@GetMapping(path="/recipe")
	public String getSingleRecipePage(final Map<String, Object> model,
			@RequestParam(required = true, name = "id") Long id) {
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
