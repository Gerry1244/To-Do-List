package com.libertymutual.goforcode.todolist.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {

	ArrayList<ToDoItem> listOfItems;
	private int nextId = 1;

	public ToDoItemRepository() {

		listOfItems = new ArrayList<ToDoItem>();
	}

	/**
	 * Get all the items from the file.
	 * 
	 * @return A list of the items. If no items exist, returns an empty list.
	 */
	public List<ToDoItem> getAll() { // working properly//
		if (listOfItems.size() == 0) {
			
			int biggestnumber = 0;

			try (FileReader fr = new FileReader("To-do-list.csv")) {

				Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr).getRecords();
				for (CSVRecord iterate : records) {
					int id = Integer.parseInt(iterate.get(0));
					String text = iterate.get(1);
					Boolean isComplete = Boolean.parseBoolean(iterate.get(2));

					ToDoItem toDoIt = new ToDoItem();
					toDoIt.setId(id);
					toDoIt.setText(text);
					toDoIt.setComplete(isComplete);
					listOfItems.add(toDoIt);
					
					if (id > biggestnumber) {
						biggestnumber = id;
					}

				}
				
				nextId = biggestnumber + 1;
			} catch (FileNotFoundException e) {
				System.out.println("fileNotFound");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listOfItems;
	}

	/**
	 * Assigns a new id to the ToDoItem and saves it to the file.
	 * 
	 * @param item
	 *            The to-do item to save to the file.
	 */
	public void create(ToDoItem item) { // working properly//
		item.setId(nextId);
		nextId = 1 + nextId;
		listOfItems.add(item);

		FileWriter writer;
		try {
			writer = new FileWriter("To-do-list.csv", true);
			CSVPrinter printer = CSVFormat.DEFAULT.print(writer);

			String[] arrayList = new String[3];
			arrayList[0] = String.valueOf(item.getId());
			arrayList[1] = item.getText();
			arrayList[2] = String.valueOf(item.isComplete());

			printer.printRecord(arrayList);
			printer.close();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();

		}

	}

	/**
	 * Gets a specific ToDoItem by its id.
	 * 
	 * @param id
	 *            The id of the ToDoItem.
	 * @return The ToDoItem with the specified id or null if none is found.
	 */
	public ToDoItem getById(int id) {

		ToDoItem item = new ToDoItem();
		try (FileReader fr = new FileReader("To-do-list.csv");) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr);

			for (CSVRecord iterate : records) {

				if (id == Integer.valueOf(iterate.get(0))) {
					item.setId(Integer.valueOf(iterate.get(0)));
					item.setText(iterate.get(1));
					item.setComplete(Boolean.parseBoolean(iterate.get(2)));
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File still not found");
			e.printStackTrace();

		}

		;
		return item;
	}

	/**
	 * Updates the given to-do item in the file.
	 * 
	 * @param item
	 *            The item to update.
	 */
	public void update(ToDoItem item) {

		for (ToDoItem thing : listOfItems) {
			if (item.getId() == thing.getId()) {
				boolean plus = item.isComplete();
				thing.setComplete(plus);
			}
		}

		try {
			FileWriter wrt = new FileWriter("To-do-list.csv");

			CSVPrinter printer = CSVFormat.DEFAULT.print(wrt);

			for (ToDoItem thing : listOfItems) {

				Object[] arrayList = new String[] { String.valueOf(thing.getId()), thing.getText(),
						String.valueOf(thing.isComplete()) };

				printer.printRecord(arrayList);

			}
			printer.close();
			wrt.close();
		} catch (IOException e) {
			System.out.println("File still not found");
			e.printStackTrace();

		}
	}
}