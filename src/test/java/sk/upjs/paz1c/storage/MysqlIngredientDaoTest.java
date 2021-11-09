package sk.upjs.paz1c.storage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

class MysqlIngredientDaoTest {
	private IngredientDao ingredientDao;
	
	public MysqlIngredientDaoTest() {
		DaoFactory.INSTANCE.testing();
		ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	}
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetAll() {
		fail("Not yet implemented");
	}
	
	@Test
	void testSave() {
		Ingredient i = new Ingredient("testing", 0.01, "100 g");
		fail("Not yet implemented");
	}
	
	@Test
	void testDelete() {
		fail("Not yet implemented");
	}
	
	@Test
	void testGetById() {
		fail("Not yet implemented");
	}
	

}
