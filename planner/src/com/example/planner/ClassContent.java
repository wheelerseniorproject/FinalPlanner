package com.example.planner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassContent implements Serializable {

	public static List<ClassItem> ITEMS = new ArrayList<ClassItem>();

	public static Map<String, ClassItem> ITEM_MAP = new HashMap<String, ClassItem>();

	public static void addItem(ClassItem item) {
		System.out.println("ITEM ADDED");
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static void updateList(ArrayList<ClassItem> newList) {
		for (ClassItem item : newList) {
			Boolean isInList = false;
			for (ClassItem oldItem : ITEMS) {
				if (oldItem.id.equals(item.id)) {
					isInList = true;
				}

				for (Assignment assignment : item.ASSIGNMENTS) {
					for (Assignment oldAssignment : oldItem.ASSIGNMENTS) {
						if (assignment.name.equals(oldAssignment.name)
								&& assignment.date.toString().equals(
										oldAssignment.date.toString())) {
							item.ASSIGNMENTS.remove(assignment);
						}
					}
				}
			}

			if (!isInList) {
				ITEMS.add(item);
				isInList = true;
			}

		}
	}

	public static void deleteClass(String deleteID) {
		for (ClassItem item : ITEMS) {
			if (item.id.equals(deleteID)) {
				ITEMS.remove(item);
			}
		}
	}

	public static class ClassItem implements Serializable {
		public String id;
		public String content;
		public String test;

		ArrayList<Integer> assID = new ArrayList<Integer>();

		ArrayList<Assignment> ASSIGNMENTS = new ArrayList<Assignment>();

		public ClassItem(String id, String content, String test) {
			this.id = id;
			this.content = content;
			this.test = test;
		}

		@Override
		public String toString() {
			return content;
		}
	}

	public static class Assignment implements Serializable, Comparable {
		String name;
		SimpleDate date;
		int priority, id;

		public Assignment(String name, SimpleDate date, int priority) {
			this.name = name;
			this.date = date;
			this.priority = priority;

		}

		public int compareTo(Object assignment) {
			int compareweight = ((Assignment) assignment).date.getWeight();
			return this.date.getWeight() - compareweight;
		}

		public static void markAsCompleted(Assignment assign) {
			assign.priority = 0;
		}

	}

	public static class SimpleDate {
		int dueYear, dueMonth, dueDay;
		String year, month, day;

		public int getWeight() {
			int weight = 100 * dueYear + 10 * dueMonth + dueDay;
			return weight;
		}

		public SimpleDate(int dueYear, int dueMonth, int dueDay) {
			this.dueYear = dueYear;
			this.dueMonth = dueMonth;
			this.dueDay = dueDay;

			day = (new Integer(dueDay)).toString();
			year = (new Integer(dueYear)).toString();

			switch (dueMonth) {
			case 0:
				month = "Jan";
				break;
			case 1:
				month = "Feb";
				break;
			case 2:
				month = "Mar";
				break;
			case 3:
				month = "Apr";
				break;
			case 4:
				month = "May";
				break;
			case 5:
				month = "Jun";
				break;
			case 6:
				month = "Jul";
				break;
			case 7:
				month = "Aug";
				break;
			case 8:
				month = "Sep";
				break;
			case 9:
				month = "Oct";
				break;
			case 10:
				month = "Nov";
				break;
			case 11:
				month = "Dec";
				break;			
			}
		}

		public String toString() {
			String str = "";

			str = month + " " + day + ", " + year;

			return str;
		}
	}
}
