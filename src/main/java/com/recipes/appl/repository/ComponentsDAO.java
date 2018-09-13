package com.recipes.appl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipes.appl.model.dbo.Component;

/**
 * @author Kastalski Sergey
 */
public interface ComponentsDAO extends JpaRepository<Component, Long> {
	
}
