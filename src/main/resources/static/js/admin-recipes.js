function loadRecipesPage() {
	makeActiveTab('recipes');
	
	$("div.left-items-container div.item span.item-link").click(fillRecipeInputsEvent);
	$("div.left-items-container div.item span.remove-from-list").click(removeRecipeEvent);
	
	loadImageProcessing();
}

function loadImageProcessing() {
	var encodeImageFileAsURL = function (createImage) {
		return function() {
			var file = this.files[0];
			var reader  = new FileReader();
			reader.onloadend = function () {
				createImage(reader.result);
			}
			reader.readAsDataURL(file);
		}
	}
	
	$("div.right-inputs-column div.recipe-image-pod input#image-file-upload").change(encodeImageFileAsURL(function(base64Img) {
		createRecipeImage(base64Img);
	}));
}

var removeRecipeEvent = function() {
	cleanInputs();
	
	var $this = $(this);
	var id = $this.parent().find("span.item-link").attr("id");
	
	$.ajax({
		url: getContextPath() + 'admin/recipe?' + $.param( {"id": id} ),
		method: 'delete',
		success: function (response) {
			var $foundItem = $("div.left-items-container div.item span.item-link[id = " + id + "]").parent();
			$foundItem.remove();
		},
		error: errorsHandlerEvent
	});
};

var fillRecipeInputsEvent = function() {
	cleanInputs();
	
	var $this = $(this);
	
	cleanActiveness();
	$this.parent().addClass("active");
	
	var recipeId = $this.attr('id');
	
	$.ajax({
		url: getContextPath() + 'admin/recipe?' + $.param( {"id": recipeId} ),
		method: 'get',
		success: function (response) {
			var recipeName = response.name;
			var recipeImage = response.image;
			var dishType = response.dishType;
			var description = response.description;
			var unescapedDescription = _.unescape(description);
			
			$("div.right-inputs-column input[name = 'recipe_id']").val(recipeId);
			$("div.right-inputs-column input[name = 'recipe_name']").val(recipeName);
			$("div.right-inputs-column select[name = 'dishtype']").find("option[value = " + dishType.id + "]").prop("selected", "selected");
			if (recipeImage) {
				createRecipeImage(recipeImage);
			}
			
			var $ingredientMeasureSelect = $("div.right-inputs-column fieldset.inputs-set div.hidden select#ingredient_count_measure");
			var $container = $("div.right-inputs-column div#ingredients-list");
			
			var ingredientsArray = response.ingredients;
			if (Array.isArray(ingredientsArray)) {
				for (var i in ingredientsArray) {
					var ingredient = ingredientsArray[i];
					
					var recipeIngredientId = ingredient.id;
					var ingredientNameId = ingredient.ingredientNameId;
					var ingredientName = ingredient.name;
					var ingredientCount = ingredient.count;
					var ingredientMeasureId = ingredient.measure.id;
					
					var $newItem = createIngredientItem($ingredientMeasureSelect.clone(), recipeIngredientId, ingredientNameId, ingredientName, ingredientMeasureId, ingredientCount);
					$container.append($newItem);
				}
			}
			
			$("div.right-inputs-column div.html-editor div.nicEdit-main").append($.parseHTML(unescapedDescription));
		},
		error: errorsHandlerEvent
	});
}

var removeIngredientEvent = function() {
	$(this).parent().parent().remove();
};

function cleanInputs() {
	cleanErrorsArea();
	
	cleanActiveness();
	
	$("div.right-inputs-column input[name = 'recipe_id']").val("");
	$("div.right-inputs-column input[name = 'recipe_name']").val("");
	$("div.right-inputs-column div.recipe-image-pod div#image-container").html("");
	$("div.right-inputs-column select[name = 'dishtype']").prop('selectedIndex', 0);
	
	$("div.right-inputs-column fieldset.inputs-set select#ingredients").prop('selectedIndex', 0);
	
	$("div.right-inputs-column div#ingredients-list div.item").remove();
	
	$("div.right-inputs-column div.html-editor div.nicEdit-main").empty();
}

function saveRecipe() {
	cleanErrorsArea();
	
	var recipeId = $("div.right-inputs-column input[name = 'recipe_id']").val();
	var recipeName = $("div.right-inputs-column input[name = 'recipe_name']").val();
	
	var $dishtypeOption = $("div.right-inputs-column select[name = 'dishtype']").find(":selected");
	var dishtypeId = $dishtypeOption.val();
	var dishtypeName = $dishtypeOption.text();
	
	var $ingredientsItems = $("div.right-inputs-column fieldset.inputs-set div#ingredients-list div.item");
	
	var description = $("div.right-inputs-column div.html-editor div.nicEdit-main").html();
	var escapedDescription = _.escape(description);
	
	var $imageContainer = $("div.right-inputs-column div.recipe-image-pod div#image-container img#recipe-image");
	var image = "";
	if ($imageContainer.attr("src") !== undefined) {
		image = $imageContainer.attr("src");
	}
	
	var data = {
		"id": recipeId, 
		"dishType": { 
			"id": dishtypeId, 
			"name": dishtypeName 
		}, 
		"name": recipeName, 
		"image": image,
		"ingredients": [], 
		"description": escapedDescription
	};
	
	$ingredientsItems.each(function(index, item) {
		var $item = $(item);
		var $ingredient = $item.find("span.ingredient");
		var ingredientCount = $item.find("div.measure-container input.ingredient-count").val();
		var $ingredientMeasureOption = $item.find("div.measure-container select[name = 'ingredient_count_measure']").find(":selected");
		var ingredientMeasureId = $ingredientMeasureOption.val();
		var ingredientMeasureValue = $ingredientMeasureOption.text();
		
		data.ingredients.push({
			'id': $ingredient.attr("id"),
			'ingredientNameId': $ingredient.attr("ingredientNameId"),
			'name': $ingredient.text(),
			'count': ingredientCount,
			'measure': {
				'id': ingredientMeasureId,
				'name': ingredientMeasureValue
			}
		});
	});
	
	$.ajax({
		url: getContextPath() + 'admin/recipe',
		method: 'post',
		data: JSON.stringify(data),
		contentType: 'application/json; charset=utf-8',
		success: function (response) {
			updateRecipes(response);
		},
		error: errorsHandlerEvent
	});
}

function addIngredient() {
	var $ingredientsSelect = $("div.right-inputs-column fieldset.inputs-set select#ingredients");
	var $ingredientOption = $ingredientsSelect.find(":selected");
	var ingredientNameId = $ingredientOption.val();
	var ingredientName = $ingredientOption.text();
	
	if (ingredientNameId == "null") {
		return;
	}
	
	var $ingredientMeasureSelect = $("div.right-inputs-column fieldset.inputs-set div.hidden select#ingredient_count_measure");
	
	var $container = $("div.right-inputs-column fieldset.inputs-set div#ingredients-list");
	var $newItem = createIngredientItem($ingredientMeasureSelect.clone(), "", ingredientNameId, ingredientName, "null", "");
	$container.append($newItem);
	
	// reset inputs
	$ingredientsSelect.prop('selectedIndex', 0);
}

function createIngredientItem($ingredientMeasureSelect, recipeIngredientId, ingredientNameId, ingredientName, ingredientMeasureId, ingredientCount) {
	var $newItem = $('<div/>', { 'class': 'item' });
	
	var $span = $('<span/>', { 'id': recipeIngredientId, 'ingredientNameId': ingredientNameId, 'class': 'ingredient' }).text(ingredientName);
	var $measureContainer = $('<div/>', { 'class': 'measure-container' });
	
	var $ingredientCountInput = $('<input/>', { 'type': 'text', 'class': 'ingredient-count'}).val(ingredientCount);
	var $whiteSpaceSpan = $('<span/>', { 'class': 'whitespace' }).text(" ");
	var $removeFromListSpan = $('<span/>', { 'class': 'remove-from-list' }).text("x");
	$removeFromListSpan.click(removeIngredientEvent);
	
	$ingredientMeasureSelect.removeAttr("id");
	$ingredientMeasureSelect.find("option[value=" + ingredientMeasureId + "]").attr("selected", "selected");
	
	$measureContainer.append($ingredientCountInput);
	$measureContainer.append($ingredientMeasureSelect);
	$measureContainer.append($whiteSpaceSpan);
	$measureContainer.append($removeFromListSpan);
	
	$newItem.append($span);
	$newItem.append($measureContainer);
	
	return $newItem;
}

function updateRecipes(recipe) {
	var id = recipe.id;
	var name = recipe.dishType.name + " " + recipe.name;
	
	var $container = $("div.left-items-container");
	var $recipes = $container.find("div.item");
	var $span = $recipes.find("span.item-link[id = " + id + "]");
	if ($span.length) { // update existing recipe
		$span.attr("id", id);
		$span.text(name);
	} else { // create new recipe
		var $newItem = $('<div/>', { 'class': 'item active' });
		$container.prepend($newItem);
		
		var $itemLink = $('<span/>', { 'id': id, 'class': 'item-link' }).text(name);
		$itemLink.click(fillRecipeInputsEvent);
		var $removeItemLink = $('<span/>', { 'class': 'remove-from-list' }).text("x");
		$removeItemLink.click(removeRecipeEvent);
		
		$newItem.append($itemLink);
		$newItem.append($removeItemLink);
		
		$("div.right-inputs-column input[name = 'recipe_id']").val(id);
	}
}

function createRecipeImage(base64Img) {
	var $imageContainer = $("div.right-inputs-column div.recipe-image-pod div#image-container");
	$imageContainer.html("");
	var $recipeImage = $('<img/>', { 'id': "recipe-image", "src": base64Img });
	$imageContainer.append($recipeImage);
}
