package com.recipes.appl.model.dbo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
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
@Table(name = "Ingredient")
public class IngredientDbo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@NotNull
	@Column(name = "Name")
	private String name;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinTable(
		name = "IngredientComponent", 
		joinColumns = { @JoinColumn(name = "Ingredient_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "Component_ID") }
	)
	private List<ComponentDbo> components;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID")
	private RecipeIngredientDbo recipeIngredient;
	
}
