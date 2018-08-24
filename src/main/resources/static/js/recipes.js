function loadRecipesPage() {
	makeActiveTab('recipes');
	
	bkLib.onDomLoaded(function() {
		nicEditors.allTextAreas();
	});
}