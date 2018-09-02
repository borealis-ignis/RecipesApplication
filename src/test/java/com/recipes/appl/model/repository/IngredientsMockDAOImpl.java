package com.recipes.appl.model.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.recipes.appl.model.dbo.Component;
import com.recipes.appl.model.dbo.Ingredient;

public class IngredientsMockDAOImpl implements IngredientsDAO {

	@Override
	public void deleteAllInBatch() {
	}

	@Override
	public void deleteInBatch(Iterable<Ingredient> arg0) {
	}

	@Override
	public List<Ingredient> findAll() {
		return null;
	}

	@Override
	public List<Ingredient> findAll(Sort arg0) {
		return null;
	}

	@Override
	public <S extends Ingredient> List<S> findAll(Example<S> arg0) {
		return null;
	}

	@Override
	public <S extends Ingredient> List<S> findAll(Example<S> arg0, Sort arg1) {
		return null;
	}

	@Override
	public List<Ingredient> findAllById(Iterable<Long> arg0) {
		return null;
	}

	@Override
	public void flush() {
	}

	@Override
	public Ingredient getOne(Long arg0) {
		return null;
	}

	@Override
	public <S extends Ingredient> List<S> saveAll(Iterable<S> arg0) {
		return null;
	}

	@Override
	public <S extends Ingredient> S saveAndFlush(S arg0) {
		return null;
	}

	@Override
	public Page<Ingredient> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Ingredient> S save(S entity) {
		return null;
	}

	@Override
	public Optional<Ingredient> findById(Long id) {
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void delete(Ingredient entity) {
	}

	@Override
	public void deleteAll(Iterable<? extends Ingredient> entities) {
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public <S extends Ingredient> Optional<S> findOne(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Ingredient> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Ingredient> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends Ingredient> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public List<Ingredient> selectIngredients() {
		final List<Ingredient> ingredients = new ArrayList<>();
		
		final Ingredient ingredient1 = new Ingredient();
		ingredient1.setId(1L);
		ingredient1.setName("Болгарский перец");
		ingredients.add(ingredient1);
		
		final Ingredient ingredient2 = new Ingredient();
		ingredient2.setId(2L);
		ingredient2.setName("Капуста");
		
		final List<Component> components = new ArrayList<>();
		final Component component1 = new Component();
		component1.setId(1L);
		component1.setName("Калий");
		component1.setIngredients(ingredients);
		
		final Component component2 = new Component();
		component2.setId(2L);
		component2.setName("Кальций");
		component2.setIngredients(ingredients);
		
		components.add(component1);
		components.add(component2);
		
		ingredient2.setComponents(components);
		
		ingredients.add(ingredient2);
		
		return ingredients;
	}

	@Override
	public List<Ingredient> findAllByName(String name) {
		return Collections.<Ingredient>emptyList();
	}
	
}
