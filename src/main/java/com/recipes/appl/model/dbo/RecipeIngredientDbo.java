package com.recipes.appl.model.dbo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "RecipeIngredient")
public class RecipeIngredientDbo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "Recipe_ID", nullable = false)
	private RecipeDbo recipe;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Ingredient_ID", unique=true, nullable=false)
	private IngredientDbo ingredient;
	
	@NotNull
	@Column(name = "Count")
	private Double count;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IngredientMeasure_ID", unique=true, nullable=false)
	private IngredientMeasureDbo ingredientMeasure;
	
	@Override
	public String toString() {
		return id + ". " + ingredient.getName() + ": " + count + " " + ingredientMeasure.getName();
	}
}
