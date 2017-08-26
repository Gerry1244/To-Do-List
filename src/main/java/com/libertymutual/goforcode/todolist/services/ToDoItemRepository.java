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
	
	public List<ToDoItem> getAll() { 
		// This checks the size of the list and only runs if it is 0.
		if (listOfItems.size() == 0) {
			
			int biggestnumber = 0;
			//creates the file reader for the getAll method.
			try (FileReader fr = new FileReader("To-do-list.csv")) {
				//this line is creating a list of records for the CSV file. 
				Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr).getRecords();
				//this section indicates that for each record, it sets the value for each and adds to the file
				for (CSVRecord iterate : records) {
					//
					int id = Integer.parseInt(iterate.get(0));
					String text = iterate.get(1);
					Boolean isComplete = Boolean.parseBoolean(iterate.get(2));
					//this creates a new "ToDoItem" for each record
					ToDoItem toDoIt = new ToDoItem();
					toDoIt.setId(id);
					toDoIt.setText(text);
					toDoIt.setComplete(isComplete);
					listOfItems.add(toDoIt);
					
					if (id > biggestnumber) {
						biggestnumber = id;
					}

				}
				//this line will continue to add records sequentially if the ID returned is greater than the largest (biggestnumber).
				nextId = biggestnumber + 1;
			} catch (FileNotFoundException e) {
				//This line indicates that the message 'filenotfound' will be displayed if the FileReader cannot find the file
				System.out.println("fileNotFound");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// This is returning my list of items
		return listOfItems;
	}

	/**
	 * Assigns a new id to the ToDoItem and saves it to the file.
	 * 
	 * @param item
	 *            The to-do item to save to the file.
	 */
	public void create(ToDoItem item) { 
		//This is setting the ID and adding 1 to the next ID to get the items set sequentially
		item.setId(nextId);
		nextId = 1 + nextId;
		listOfItems.add(item);

		FileWriter writer;
		try {
			//This line is writing the to the appropriate file
			writer = new FileWriter("To-do-list.csv", true);
			CSVPrinter printer = CSVFormat.DEFAULT.print(writer);

		//	
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
		//
		ToDoItem item = new ToDoItem();
		//this line indicates the fileReader is reading the CSV file
		try (FileReader fr = new FileReader("To-do-list.csv");) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(fr);

			for (CSVRecord iterate : records) {
				//would like to review this section again...
				if (id == Integer.valueOf(iterate.get(0))) {
					item.setId(Integer.valueOf(iterate.get(0)));
					item.setText(iterate.get(1));
					item.setComplete(Boolean.parseBoolean(iterate.get(2)));
				}
			}
			//This line indicates that if the file cannot be found--then it will print out the file is not available
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
		//this line indicates a loop is looking for a match to the ID and completing if it is true
		for (ToDoItem thing : listOfItems) {
			if (item.getId() == thing.getId()) {
				boolean plus = item.isComplete();
				thing.setComplete(plus);
			}
		}

		try {
			//this line indicates the fileWriter is writing the list of items to the file.
			FileWriter wrt = new FileWriter("To-do-list.csv");
			
			CSVPrinter printer = CSVFormat.DEFAULT.print(wrt);
			 
			for (ToDoItem thing : listOfItems) {
				//Would like to review a bit more...
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