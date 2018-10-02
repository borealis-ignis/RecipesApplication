$(document).ready(function() {
	$("div.filters-column input#search-button").click(searchRecipesEvent);
	$("div.filters-column select#ingredients").change(selectIngredientEvent);
	$("div.filters-column select#components").change(selectComponentEvent);
	
	$("div.filters-column div#ingredients-list div.item span.remove-from-list").click(removeSearchItemEvent);
	$("div.filters-column div#components-list div.item span.remove-from-list").click(removeSearchItemEvent);
});

var searchRecipesEvent = function() {
	var recipePartName = $("div.filters-column input.recipename").val();
	
	var recipeDishTypeId = $("div.filters-column select[name = 'dishtype']").find(":selected").val();
	if (recipeDishTypeId == "null") {
		recipeDishTypeId = "";
	}
	
	var $ingredientItems = $("div.filters-column div#ingredients-list div.item");
	var ingredientIdsString = prepareIdsString($ingredientItems);
	
	var $componentItems = $("div.filters-column div#components-list div.item");
	var componentIdsString = prepareIdsString($componentItems);
	
	var uri = "recipes?name=" + encodeURIComponent(recipePartName) + 
					"&dishtype=" + recipeDishTypeId + 
					"&ingredients=" + encodeURIComponent(ingredientIdsString) + 
					"&components=" + encodeURIComponent(componentIdsString);
	
	window.location = getContextPath() + uri;
}

var selectIngredientEvent = function() {
	var $this = $(this);
	var $container = $("div.filters-column div#ingredients-list");
	addSearchItem($this, $container);
}

var selectComponentEvent = function() {
	var $this = $(this);
	var $container = $("div.filters-column div#components-list");
	addSearchItem($this, $container);
}

var removeSearchItemEvent = function() {
	var $this = $(this);
	$this.parent().remove();
}


function addSearchItem($currentSelect, $container) {
	var id = $currentSelect.children(":selected").val();
	var name = $currentSelect.children(":selected").text();
	$currentSelect.prop('selectedIndex', 0);
	
	if ($container.find("div.item[id = " + id + "]").length) {
		return;
	}
	
	$container.prepend(createSearchItem(id, name));
}

function createSearchItem(id, name) {
	var $itemDiv = $('<div/>', { 'class': 'item', 'id': id });
	var $nameSpan = $('<span/>').text(name);
	var $removeFromListSpan = $('<span/>', { 'class': 'remove-from-list' }).text("x");
	$removeFromListSpan.click(removeSearchItemEvent);
	
	$itemDiv.append($nameSpan);
	$itemDiv.append($removeFromListSpan);
	
	return $itemDiv;
}

function prepareIdsString($items) {
	var result = "";
	$items.each(function(index, item) {
		var $item = $(item);
		var id = $item.attr("id");
		result += id + ";";
	});
	return result;
}