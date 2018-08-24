package com.recipes.appl.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Kastalski Sergey
 */
@Controller
public class IngredientsAdminController {
	
	@GetMapping(path="/admin/ingredients")
	public String getAdminIngredientsPage(final Map<String, Object> model) {
		return "admin/ingredients";
	}
	
}
