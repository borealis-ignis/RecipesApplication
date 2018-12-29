package com.recipes.appl.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.recipes.appl.converter.impl.DishTypeConverter;
import com.recipes.appl.converter.impl.IngredientMeasureConverter;
import com.recipes.appl.converter.impl.RecipeConverter;
import com.recipes.appl.model.dbo.RecipeDbo;
import com.recipes.appl.model.dbo.RecipeIngredientDbo;
import com.recipes.appl.model.dto.DishTypeDto;
import com.recipes.appl.model.dto.IngredientDto;
import com.recipes.appl.model.dto.IngredientMeasureDto;
import com.recipes.appl.model.dto.RecipeDto;
import com.recipes.appl.model.dto.errors.DishTypeError;
import com.recipes.appl.model.dto.errors.MeasureError;
import com.recipes.appl.model.dto.errors.RecipeError;
import com.recipes.appl.repository.DishTypesDAO;
import com.recipes.appl.repository.IngredientMeasuresDAO;
import com.recipes.appl.repository.RecipeIngredientDAO;
import com.recipes.appl.repository.RecipesDAO;
import com.recipes.appl.util.ImagesUtil;


/**
 * @author Kastalski Sergey
 */
@Service
public class RecipesService extends AbstractService {
	
	private static Logger logger = LoggerFactory.getLogger(RecipesService.class);
	
	private DishTypesDAO dishTypesDAO;
	
	private IngredientMeasuresDAO ingredientMeasuresDAO;
	
	private RecipesDAO recipesDAO;
	
	private RecipeIngredientDAO recipeIngredientDAO;
	
	private DishTypeConverter dishTypeConverter;
	
	private RecipeConverter recipeConverter;
	
	private IngredientMeasureConverter ingredientMeasureConverter;
	
	private final int maxWidth;
	private final int maxHeight;
	
	@Autowired
	public RecipesService(
			final DishTypesDAO dishTypesDAO,
			final IngredientMeasuresDAO ingredientMeasuresDAO,
			final RecipesDAO recipesDAO,
			final RecipeIngredientDAO recipeIngredientDAO,
			final DishTypeConverter dishTypeConverter,
			final RecipeConverter recipeConverter,
			final IngredientMeasureConverter ingredientMeasureConverter,
			@Value("${uploading.image.max.width:500}") final Integer maxWidth,
			@Value("${uploading.image.max.height:350}") final Integer maxHeight) {
		this.dishTypesDAO = dishTypesDAO;
		this.ingredientMeasuresDAO = ingredientMeasuresDAO;
		this.recipesDAO = recipesDAO;
		this.recipeIngredientDAO = recipeIngredientDAO;
		this.dishTypeConverter = dishTypeConverter;
		this.recipeConverter = recipeConverter;
		this.ingredientMeasureConverter = ingredientMeasureConverter;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	
	public List<DishTypeDto> getDishTypes() {
		return dishTypeConverter.convertDboToDto(dishTypesDAO.findAllByOrderByIdAsc());
	}
	
	public List<IngredientMeasureDto> getIngredientMeasures() {
		return ingredientMeasureConverter.convertDboToDto(ingredientMeasuresDAO.findAllByOrderByIdAsc());
	}
	
	public List<RecipeDto> getRecipes() {
		return recipeConverter.convertDboToDto(recipesDAO.findAll());
	}
	
	public List<RecipeDto> searchRecipes(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList, final Pageable pageable) {
		return recipeConverter.convertDboToDto(recipesDAO.findAllByFilterParams(namePart, dishTypeId, ingredientIdList, componentIdList, pageable));
	}
	
	public long recipesCount(final String namePart, final Long dishTypeId, final List<Long> ingredientIdList, final List<Long> componentIdList) {
		return recipesDAO.recipesCount(namePart, dishTypeId, ingredientIdList, componentIdList);
	}
	
	public RecipeDto getRecipe(final Long id) {
		return recipeConverter.convertDboToDto(recipesDAO.findById(id).orElse(new RecipeDbo()));
	}
	
	public RecipeDto saveRecipe(final RecipeDto recipe) {
		return recipeConverter.convertDboToDto(recipesDAO.saveAndFlush(recipeConverter.convertDtoToDbo(recipe)));
	}
	
	public RecipeDto deleteRecipe(final Long id) {
		recipesDAO.deleteById(id);
		
		final RecipeDto dto = new RecipeDto();
		dto.setId(id);
		return dto;
	}
	
	public DishTypeDto saveDishType(final DishTypeDto dishType) {
		return dishTypeConverter.convertDboToDto(dishTypesDAO.saveAndFlush(dishTypeConverter.convertDtoToDbo(dishType)));
	}
	
	public IngredientMeasureDto saveIngredientMeasure(final IngredientMeasureDto measure) {
		return ingredientMeasureConverter.convertDboToDto(ingredientMeasuresDAO.saveAndFlush(ingredientMeasureConverter.convertDtoToDbo(measure)));
	}
	
	public DishTypeDto deleteDishType(final Long id) {
		dishTypesDAO.deleteById(id);
		
		final DishTypeDto dto = new DishTypeDto();
		dto.setId(id);
		return dto;
	}
	
	public IngredientMeasureDto deleteMeasure(final Long id) {
		ingredientMeasuresDAO.deleteById(id);
		
		final IngredientMeasureDto dto = new IngredientMeasureDto();
		dto.setId(id);
		return dto;
	}
	
	public ResponseEntity<RecipeDto> validateRecipe(final RecipeDto recipe) {
		final String recipeName = recipe.getName();
		if (StringUtils.isBlank(recipeName)) {
			return getErrorResponseMessage("admin.recipe.error.noname", RecipeError.class);
		}
		
		final DishTypeDto dishType = recipe.getDishType();
		if (dishType == null || dishType.getId() == null) {
			return getErrorResponseMessage("admin.recipe.error.nodishtype", RecipeError.class);
		}
		
		final List<IngredientDto> ingredients = recipe.getIngredients();
		if (CollectionUtils.isEmpty(ingredients)) {
			return getErrorResponseMessage("admin.recipe.error.noingredients", RecipeError.class);
		}
		
		for (final IngredientDto ingredient : ingredients) {
			final Double count = ingredient.getCount();
			final IngredientMeasureDto measure = ingredient.getMeasure();
			if (count == null || measure == null || measure.getId() == null) {
				return getErrorResponseMessage("admin.recipe.error.nomeasure", RecipeError.class);
			}
		}
		
		
		// add new recipe
		if (recipe.getId() == null) {
			final List<RecipeDbo> foundRecipes = recipesDAO.findAllByNameAndDishType(recipeName, dishType.getId());
			if (!foundRecipes.isEmpty()) {
				return getErrorResponseMessage("admin.recipe.error.notunique.name", RecipeError.class);
			}
		}
		
		if (StringUtils.isNotBlank(recipe.getImage())) {
			try {
				recipe.setImage(ImagesUtil.compressImage(recipe.getImage(), maxWidth, maxHeight));
			} catch (final IOException e) {
				logger.error("Error image compression", e);
				return getErrorResponseMessage("admin.recipe.error.image.processing", RecipeError.class);
			}
		}
		
		return null;
	}
	
	public ResponseEntity<DishTypeDto> validateDishType(final DishTypeDto dishType) {
		if (StringUtils.isBlank(dishType.getName())) {
			return getErrorResponseMessage("admin.statics.error.blank.dishtype", DishTypeError.class);
		}
		
		return null;
	}
	
	public ResponseEntity<IngredientMeasureDto> validateMeasure(final IngredientMeasureDto measure) {
		if (StringUtils.isBlank(measure.getName())) {
			return getErrorResponseMessage("admin.statics.error.blank.measure", MeasureError.class);
		}
		
		return null;
	}
	
	public ResponseEntity<DishTypeDto> validateDishtypeBeforeDelete(final Long id) {
		final List<RecipeDbo> recipesList = recipesDAO.findAllByDishTypeId(id);
		if (!recipesList.isEmpty()) {
			return getErrorResponseMessage("admin.dishtype.error.part.of.recipe", DishTypeError.class);
		}
		
		return null;
	}
	
	public ResponseEntity<IngredientMeasureDto> validateMeasureBeforeDelete(final Long id) {
		final List<RecipeIngredientDbo> recipeIngredientsList = recipeIngredientDAO.findAllByMeasureId(id);
		if (!recipeIngredientsList.isEmpty()) {
			return getErrorResponseMessage("admin.measure.error.part.of.recipe", MeasureError.class);
		}
		
		return null;
	}
	
}
