package com.example.mdbspringboot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.CollectionUtils;

import com.example.mdbspringboot.model.GroceryItem;
import com.example.mdbspringboot.repository.ItemRepository;

@SpringBootApplication
@EnableMongoRepositories
public class MdbSpringBootApplication implements CommandLineRunner {

	@Autowired
	ItemRepository groceryItemRepo;

	private static final Logger log = LoggerFactory.getLogger(MdbSpringBootApplication.class);

	List<GroceryItem> itemList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(MdbSpringBootApplication.class, args);
	}

	public void run(String... args) {

		// Clean up any previous data
		groceryItemRepo.deleteAll(); // Doesn't delete the collection

		log.info("-------------CREATE GROCERY ITEMS-------------------------------\n");

		createGroceryItems();
		
		log.info("\n------------Initial COUNT OF GROCERY ITEMS-------------------------\n");

		findCountOfGroceryItems();

		log.info("\n----------------SHOW ALL GROCERY ITEMS---------------------------\n");

		showAllGroceryItems();

		log.info("\n--------------GET ITEM BY NAME-----------------------------------\n");

		getGroceryItemByName("Whole Wheat Biscuit");

		log.info("\n-----------GET ITEMS BY CATEGORY---------------------------------\n");

		getItemsByCategory("millets");



		log.info("\n----------DELETE A GROCERY ITEM----------------------------------\n");

		deleteGroceryItem("Kodo Millet");

		log.info("\n------------FINAL COUNT OF GROCERY ITEMS-------------------------\n");

		findCountOfGroceryItems();

		log.info("\n-------------------THANK YOU---------------------------");

	}

	// CRUD operations

	// CREATE
	void createGroceryItems() {
		log.info("Data creation started...");

		groceryItemRepo.save(new GroceryItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks"));
		groceryItemRepo.save(new GroceryItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets"));
		groceryItemRepo.save(new GroceryItem("Dried Red Chilli", "Dried Whole Red Chilli", 2, "spices"));
		groceryItemRepo.save(new GroceryItem("Pearl Millet", "Healthy Pearl Millet", 1, "millets"));
		groceryItemRepo.save(new GroceryItem("Cheese Crackers", "Bonny Cheese Crackers Plain", 6, "snacks"));

		log.info("Data creation complete...");
	}

	// READ
	// 1. Show all the data
	public void showAllGroceryItems() {

		itemList = groceryItemRepo.findAll();

		itemList.forEach(item -> log.info(getItemDetails(item)));
	}

	// 2. Get item by name
	public void getGroceryItemByName(String name) {
		log.info("Getting item by name{} ", name);
		GroceryItem item = groceryItemRepo.findItemByName(name);
		log.info(getItemDetails(item));
	}

	// 3. Get name and items of a all items of a particular category
	public void getItemsByCategory(String category) {
		log.info("Getting items for the category {} ", category);
		List<GroceryItem> list = groceryItemRepo.findAll(category);

		list.forEach(item -> log.info("Name: {} , Quantity: {}  " ,item.getName() ,item.getItemQuantity()));
	}

	// 4. Get count of documents in the collection
	public void findCountOfGroceryItems() {
		long count = groceryItemRepo.count();
		log.info("Number of documents in the collection = {} ", count);
	}

	// UPDATE APPROACH 1: Using MongoRepository
	public void updateCategoryName(String category) {

		// Change to this new value
		String newCategory = "munchies";

		// Find all the items with the category
		List<GroceryItem> list = groceryItemRepo.findAll(category);

		list.forEach(item -> {
			// Update the category in each document
			item.setCategory(newCategory);
		});

		// Save all the items in database
		List<GroceryItem> itemsUpdated = groceryItemRepo.saveAll(list);

		if (!CollectionUtils.isEmpty(itemsUpdated)) {
			log.info("Successfully updated {}  items.",itemsUpdated.size());
		}
	}

	// DELETE
	public void deleteGroceryItem(String id) {
		groceryItemRepo.deleteById(id);
		log.info("Item with id {} deleted...",id);
	}
	// Print details in readable form

	public String getItemDetails(GroceryItem item) {

		log.info("Item Name: {} , Item Quantity: {} , Item Category: {} ",item.getName(),item.getItemQuantity(),item.getCategory());

		return "";
	}
}
