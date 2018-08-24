package com.recipes.appl.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Kastalski Sergey
 */
@Controller
public class RecipesAdminController {
	
	@GetMapping(path="/admin/recipes")
	public String getAdminRecipesPage(final Map<String, Object> model) {
		return "admin/recipes";
	}
	
}
