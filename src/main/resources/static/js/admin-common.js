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