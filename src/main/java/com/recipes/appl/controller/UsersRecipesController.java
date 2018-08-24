package com.recipes.appl.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Kastalski Sergey
 */
@Controller
public class UsersRecipesController {
	
	@GetMapping(path="/recipes")
	public String getRecipesPage(final Map<String, Object> model) {
		return "recipes";
	}
	
	@GetMapping(path="/recipe")
	public String getSingleRecipePage(final Map<String, Object> model) {
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
