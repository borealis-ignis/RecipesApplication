package com.recipes.appl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipes.appl.model.dbo.ComponentDbo;

/**
 * @author Kastalski Sergey
 */
public interface ComponentsDAO extends JpaRepository<ComponentDbo, Long> {
	
}
