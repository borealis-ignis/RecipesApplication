package com.recipes.appl.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.recipes.appl.MockData;
import com.recipes.appl.model.dbo.IngredientMeasureDbo;

/**
 * @author Kastalski Sergey
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class IngredientMeasuresDAOTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private IngredientMeasuresDAO measuresDAO;
	
	
	@Test
	public void findAllMeasuresByOrderByIdAsc() {
		final List<IngredientMeasureDbo> ingredientMeasuresList = MockData.listDboIngredientMeasures();
		ingredientMeasuresList.forEach(ingredientMeasure -> {
			ingredientMeasure.setId(null);
			entityManager.persist(ingredientMeasure);
		});
		
		final List<IngredientMeasureDbo> foundIngredientMeasuresList = measuresDAO.findAllByOrderByIdAsc();
		
		assertThat(foundIngredientMeasuresList).isNotEmpty().containsExactlyElementsOf(ingredientMeasuresList);
	}
	
}
