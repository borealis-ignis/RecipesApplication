package com.recipes.appl.model.dbo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Kastalski Sergey
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "Recipe")
public class RecipeDbo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DishType_ID", unique=true, nullable=false)
	private DishTypeDbo dishType;
	
	@NotNull
	@Column(name = "Name")
	private String name;
	
	@Column(name = "Image")
	private String image;
	
	@NotNull
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	private List<RecipeIngredientDbo> recipeIngredients;
	
	@Column(name = "Description")
	private String description;
	
}
