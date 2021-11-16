package sk.upjs.paz1c.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MySqlFoodDaoTest {

	private Food savedFood;
	private FoodDao foodDao;

	public MySqlFoodDaoTest() {
		DaoFactory.INSTANCE.testing();
		foodDao = DaoFactory.INSTANCE.getFoodDao();
	}

	@BeforeEach
	void setUp() throws Exception {
		Food food = new Food("test food", "made for testing", "none", 10.99, 1);
		savedFood = foodDao.save(food);

	}

	@AfterEach
	void tearDown() throws Exception {
		foodDao.delete(savedFood.getId());
	}

	@Test
	void testGetAll() {
		// OK
		List<Food> foods = foodDao.getAll();
		assertNotNull(foods);
		assertTrue(foods.size() > 0);
		System.out.println(foods); // divne IDcka?? pozriet
	}

	@Test
	void testSave() throws EntityUndeletableException {
		//OK
		// INSERT
		int initialSize = foodDao.getAll().size();
		Food newFood = new Food("TestOfSave", "idk", "image", 5.55, 500);
		Food savedNewFood = foodDao.save(newFood);
		assertEquals(savedNewFood.getName(), newFood.getName());
		assertEquals(savedNewFood.getPrice(), newFood.getPrice());
		assertEquals(savedNewFood.getWeight(), newFood.getWeight());
		assertEquals(savedNewFood.getDescription(), newFood.getDescription());
		assertEquals(savedNewFood.getImage_url(), newFood.getImage_url());
		assertEquals(savedNewFood.getIngredients(), newFood.getIngredients());
		assertNotNull(savedNewFood.getId());

		List<Food> all = foodDao.getAll();
		assertEquals(initialSize + 1, all.size());

		boolean found = false;
		for (Food food : all) {
			if (food.getId().equals(savedNewFood.getId())) {
				found = true;
				assertEquals("TestOfSave", food.getName());
				assertEquals("idk", food.getDescription());
				assertEquals("image", food.getImage_url());
				assertEquals(5.55, food.getPrice());
				assertEquals(500, food.getWeight());
				break;
			}
		}
		assertTrue(found);
		foodDao.delete(savedNewFood.getId());

		// UPDATE
		Food changedFood = new Food(savedFood.getId(),"changedFood", "changed food test", "image2", 8.00, 450);
		Food savedChangedFood = foodDao.save(changedFood);
		assertEquals(changedFood, savedChangedFood);
		assertEquals("changedFood", savedChangedFood.getName());
		assertEquals("changed food test", savedChangedFood.getDescription());
		assertEquals("image2", savedChangedFood.getImage_url());
		assertEquals(8.00, savedChangedFood.getPrice());
		assertEquals(savedChangedFood.getId(), changedFood.getId());

		all = foodDao.getAll();
		found = false;
		for (Food f : all) {
			if (f.getId().equals(changedFood.getId())) {
				found = true;
				assertEquals("changedFood", f.getName());
				assertEquals("changed food test", f.getDescription());
				assertEquals("image2", f.getImage_url());
				assertEquals(8.00, f.getPrice());
				assertEquals(450, f.getWeight());
				break;
			}
		}
		assertTrue(found);
		changedFood.setId(-1L);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.save(changedFood);
			}
		});
		assertThrows(NullPointerException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.save(null);
			}
		});
	}

	@Test
	void testSaveIngredient() throws EntityUndeletableException {
		//INSERT
		Ingredient i = new Ingredient("Test", 0.5, "5 g");
		IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
		Ingredient savedIngr = ingredientDao.save(i);
		int beforeInsert = savedFood.getIngredients().size();
		System.out.println(beforeInsert);
		foodDao.saveIngredient(savedFood, savedIngr, 8);
		System.out.println(savedFood.getIngredients().size());
		
		Map<Ingredient, Integer> all = savedFood.getIngredients();
		assertEquals(beforeInsert + 1, all.size());
		assertTrue(all.containsKey(savedIngr));
		assertTrue(all.containsValue(8));
		
		
//		Ingredient i2 = new Ingredient("tttt", 3.2, "");
//		assertThrows(EntityNotFoundException.class, new Executable() {
//			@Override
//			public void execute() throws Throwable {
//				foodDao.saveIngredient(savedFood, i2, 9);				
//			}
//		});
		
		//UPDATE
		all = foodDao.saveIngredient(savedFood, savedIngr, 10);
		assertEquals(savedFood.getIngredients(), all);
		assertTrue(all.containsKey(savedIngr));
		assertEquals(all.get(savedIngr), 10);
		
		foodDao.deleteIngredient(savedFood, savedIngr);
		ingredientDao.delete(savedIngr.getId());		
	}
	
	@Test
	void deleteIngredientTest() throws EntityUndeletableException {
		//OK
		Ingredient i = new Ingredient("Test", 0.5, "5 g");
		Ingredient i2 = new Ingredient("Test2", 0.5, "5 g");
		IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
		Ingredient savedIngr = ingredientDao.save(i);
		Ingredient savedIngr2 = ingredientDao.save(i2);
		foodDao.saveIngredient(savedFood, savedIngr, 4);
		foodDao.saveIngredient(savedFood, savedIngr2, 3);
		
		int beforeCount = savedFood.getIngredients().size();
		Food afterDelete = foodDao.deleteIngredient(savedFood, savedIngr);
		
		assertEquals(beforeCount - 1, afterDelete.getIngredients().size());
		assertFalse(afterDelete.getIngredients().containsKey(savedIngr) );
		
		foodDao.deleteIngredient(savedFood, savedIngr2);
		ingredientDao.delete(savedIngr.getId());
		ingredientDao.delete(savedIngr2.getId());
		
	}

	@Test
	void testGetById() {
		// OK
		Food byId = foodDao.getById(savedFood.getId());
		assertEquals(savedFood, byId);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.getById(-1L);
			}
		});
	}

	@Test
	void testDelete() throws EntityUndeletableException {
		// OK
		Food foodToDelete = new Food("delete", "food to be deleted", "idk", 5.00, 100);
		Food saved = foodDao.save(foodToDelete);
		Food savedToDelete = foodDao.delete(saved.getId());
		assertEquals(saved, savedToDelete);
		assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				foodDao.getById(saved.getId());
			}
		});
		// TODO otestovat ked pridame jedlo do order
//		assertThrows(EntityUndeletableException.class, new Executable() {
//			@Override
//			public void execute() throws Throwable {
//				foodDao.delete(1L);
//			}
//		});
	}

}
