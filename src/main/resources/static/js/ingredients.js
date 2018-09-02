function loadIngredientsPage() {
	makeActiveTab('ingredients');
	
	$("div.left-items-container div.item span.item-link").click(fillIngredientInputsEvent);
	$("div.left-items-container div.item span.remove-from-list").click(removeIngredientEvent);
	
	$(function() {
		var availableComponents = [];
		$("div.right-inputs-column fieldset.inputs-set items item").each(function(index, item) {
			var $item = $(item);
			availableComponents.push($item.attr("name"));
		});
		$("div.right-inputs-column input#component_names").autocomplete({
			source: availableComponents
		});
	});
}

var fillIngredientInputsEvent = function() {
	cleanInputs();
	
	var $this = $(this);
	
	cleanActiveness();
	$this.parent().addClass("active");
	
	var ingredientId = $this.attr('id');
	var ingredientName = $this.text();
	
	$("div.right-inputs-column input[name = 'ingredient_id']").val(ingredientId);
	$("div.right-inputs-column input[name = 'ingredient_name']").val(ingredientName);
	
	var $container = $("div.right-inputs-column div#components-list");
	$.ajax({
		url: '/admin/ingredient/components?' + $.param( {"id": ingredientId} ),
		method: 'get',
		success: function (response) {
			if (Array.isArray(response)) {
				for (var i in response) {
					var component = response[i];
					var $newItem = createComponentItem(component.id, component.name);
					$container.prepend($newItem);
				}
			}
		},
		error: errorsHandlerEvent
	});
};

var removeIngredientEvent = function() {
	cleanInputs();
	
	var $this = $(this);
	var id = $this.parent().find("span.item-link").attr("id");
	
	$.ajax({
		url: '/admin/ingredient/delete?' + $.param( {"id": id} ),
		method: 'delete',
		success: function (response) {
			var $foundItem = $("div.left-items-container div.item span.item-link[id = " + id + "]").parent();
			$foundItem.remove();
		},
		error: errorsHandlerEvent
	});
};

var removeComponentEvent = function() {
	$(this).parent().remove();
};


function cleanInputs() {
	cleanErrorsArea();
	
	cleanActiveness();
	
	$("div.right-inputs-column input[name = 'ingredient_id']").val("");
	$("div.right-inputs-column input[name = 'ingredient_name']").val("");
	$("div.right-inputs-column input[name = 'component_name']").val("");
	$("div#components-list div.item").remove();
}

function saveIngredient() {
	cleanErrorsArea();
	
	var ingredientId = $("div.right-inputs-column input[name = 'ingredient_id']").val();
	var ingredientName = $("div.right-inputs-column input[name = 'ingredient_name']").val();
	
	var data = { "id": ingredientId, "name": ingredientName, "components": []};
	
	var $components = $("div.right-inputs-column div#components-list input.component-item");
	$components.each(function(index, item) {
		var $item = $(item);
		data.components.push({
			'id': $item.attr("id"),
			'name': $item.val()
		});
	});
	
	$.ajax({
		url: '/admin/ingredient/save',
		method: 'post',
		data: JSON.stringify(data),
		contentType: 'application/json; charset=utf-8',
		success: function (response) {
			updateIngredients(response);
		},
		error: errorsHandlerEvent
	});
}

function updateIngredients(ingredient) {
	var id = ingredient.id;
	var name = ingredient.name;
	
	var $container = $("div.left-items-container");
	var $ingredients = $container.find("div.item");
	var $span = $ingredients.find("span.item-link[id = " + id + "]");
	if ($span.length) { // update existing ingredient
		$span.attr("id", id);
		$span.text(name);
	} else { // create new ingredient
		var $newItem = $('<div/>', { 'class': 'item active' });
		$container.prepend($newItem);
		
		var $itemLink = $('<span/>', { 'id': id, 'class': 'item-link' }).text(name);
		$itemLink.click(fillIngredientInputsEvent);
		var $removeItemLink = $('<span/>', { 'class': 'remove-from-list' }).text("x");
		$removeItemLink.click(removeIngredientEvent);
		
		$newItem.append($itemLink);
		$newItem.append($removeItemLink);
		
		$("div.right-inputs-column input[name = 'ingredient_id']").val(id);
	}
	
	refreshComponentsItems();
}

function refreshComponentsItems() {
	$.ajax({
		url: '/admin/ingredient/allcomponents',
		method: 'get',
		success: function (response) {
			var availableComponents = [];
			var $container = $("div.right-inputs-column fieldset.inputs-set items");
			$container.children().remove();
			
			for (var i in response) {
				var component = response[i];
				
				var $item = $('<item/>', { 'id': component.id, 'name': component.name });
				$container.append($item);
				
				availableComponents.push(component.name);
			}
			
			$("div.right-inputs-column input#component_names").autocomplete({
				source: availableComponents
			});
		},
		error: errorsHandlerEvent
	});
}

function addComponent() {
	var $nameInput = $("div.right-inputs-column input.component-name");
	var name = $nameInput.val();
	if (!name) {
		return;
	}
	
	var $foundItems = $("div.right-inputs-column fieldset.inputs-set items item[name='" + name + "']");
	if ($foundItems.size > 1) {
		return;
	}
	
	var id = $foundItems.attr("id");
	
	var $container = $("div.right-inputs-column div#components-list");
	var $newItem = createComponentItem(id, name);
	$container.prepend($newItem);
	
	$nameInput.val("");
}

function createComponentItem(id, name) {
	var $newItem = $('<div/>', { 'class': 'item' });
	
	var $componentInput = $('<input/>', { 'type': 'text', 'class': 'component-item', 'id': id }).val(name);
	var $removeItemLink = $('<input/>', { 'type': 'button', 'class': 'remove-from-list' }).val("x");
	$removeItemLink.click(removeComponentEvent);
	
	$newItem.append($componentInput);
	$newItem.append($removeItemLink);
	
	return $newItem;
}
