function loadStaticsPage() {
	makeActiveTab('statics');
	
	$("div.static-container div.editor input.save").click(editorSaveEvent);
	$("div.static-container div.editor input.reset").click(editorResetInputsEvent);
	
	$("div.static-container div.static-items span.item").click(itemChooseEvent);
	$("div.static-container div.static-items span.remove-from-list").click(itemRemoveEvent);
}

var editorSaveEvent = function() {
	cleanErrorsArea();
	cleanSuccessMessagesArea();
	
	var $this = $(this);
	var $container = $this.parent();
	var editorTypeId = $container.attr('id');
	var id = $container.find("input.hiddenId").val();
	var name = $container.find("input.item-input").val();
	
	var staticUrl = getStaticUrl(editorTypeId);
	
	var data = {
		"id": id,
		"name": name,
	};
	
	$.ajax({
		url: getContextPath() + 'admin/' + staticUrl,
		method: 'post',
		data: JSON.stringify(data),
		contentType: 'application/json; charset=utf-8',
		success: function (response) {
			var $staticItems = $container.parent().find("div.static-items");
			if (id == '') {
				createNewItem($staticItems, response.id, response.name);
			} else {
				var $item = $staticItems.find("div.item span.item[value='" + id + "']");
				$item.text(response.name);
			}
			successfulMessage();
			setInputs($container, "", "");
		},
		error: errorsHandlerEvent
	});
}

var editorResetInputsEvent = function() {
	var $this = $(this);
	var $container = $this.parent();
	setInputs($container, "", "");
	cleanErrorsArea();
	cleanSuccessMessagesArea();
}

var itemChooseEvent = function() {
	var $this = $(this);
	var id = $this.attr("value");
	var name = $this.text();
	
	var $editorContainer = $this.parent().parent().parent().find("div.editor");
	setInputs($editorContainer, id, name);
	cleanErrorsArea();
	cleanSuccessMessagesArea();
}

var itemRemoveEvent = function() {
	cleanErrorsArea();
	cleanSuccessMessagesArea();
	
	var $this = $(this);
	var $item = $this.parent();
	var id = $item.find("span.item").attr("value");
	
	var $editorContainer = $this.parent().parent().parent().find("div.editor");
	var editorTypeId = $editorContainer.attr("id");
	
	var staticUrl = getStaticUrl(editorTypeId);
	
	$.ajax({
		url: getContextPath() + 'admin/' + staticUrl + '?' + $.param( {"id": id} ),
		method: 'delete',
		success: function (response) {
			$item.remove();
			successfulMessage();
		},
		error: errorsHandlerEvent
	});
}

function getStaticUrl(editorTypeId) {
	if (editorTypeId === "dishtype-editor") {
		return "dishtype";
	} else if (editorTypeId === "measure-editor") {
		return "measure";
	}
	return "";
}

function setInputs($container, id, name) {
	$container.find("input.hiddenId").val(id);
	$container.find("input.item-input").val(name);
}

function createNewItem($staticItemsContainer, id, name) {
	var $newItem = $('<div/>', { 'class': 'item' });
	
	var $itemSpan = $('<span/>', { 'value': id, 'class': 'item' }).text(name);
	$itemSpan.click(itemChooseEvent);
	
	var $removeSpan = $('<span/>', { 'class': 'remove-from-list' }).text("x");
	$removeSpan.click(itemRemoveEvent);
	
	$newItem.append($itemSpan);
	$newItem.append($removeSpan);
	
	$staticItemsContainer.append($newItem);
}