function makeActiveTab(id_tab) {
	$("#" + id_tab).addClass('active');
}

function selectTab(selectedTab) {
	switch (selectedTab.id) {
		case 'ingredients':
			window.location = '/admin/ingredients';
			break;
		case 'recipes':
			window.location = '/admin/recipes';
			break;
	}
}

var errorsHandlerEvent = function (response) {
	var errorText = response.responseJSON.errorMessage;
	$("div.errors-column").text(errorText);
};

function cleanErrorsArea() {
	$("div.errors-column").text("");
}

function cleanActiveness() {
	$("div.left-items-container div.item").removeClass("active");
}