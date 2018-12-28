package com.recipes.appl.model.dto.errors;

import com.recipes.appl.model.dto.IngredientMeasureDto;

/**
 * @author Kastalski Sergey
 */
public class MeasureError extends IngredientMeasureDto implements StaticItemError {
	
	private String errorMessage;
	
	public MeasureError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
