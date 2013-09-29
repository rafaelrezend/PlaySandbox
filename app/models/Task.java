package models;

import java.util.*;
import java.util.regex.Pattern;

import play.modules.mongojack.MongoDB;

import org.mongojack.JacksonDBCollection;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import com.mongodb.BasicDBObject;

public class Task {

	@Id
	@ObjectId
	public String id;

	public boolean isPrivate;

	public String label;

	private static JacksonDBCollection<Task, String> coll = MongoDB
			.getCollection("tasks", Task.class, String.class);

	public static List<Task> all() {
		return Task.coll.find().toArray();
	}

	public static void create(Task task) {
		Task.coll.save(task);
	}

	public static void delete(String id) {
		Task task = Task.coll.findOneById(id);
		if (task != null)
			// This method looks for the @ObjectId element. A query is required
			// for removing based on other fields.
			Task.coll.removeById(id);
	}

	/**
	 * This search validates only the field "label" of task. The advantage of
	 * this is that the matching element may only contains a part of the form in
	 * its field. Not and exact match. Ex: search for "label"="123" would also
	 * returns "label"="1234".
	 * 
	 * @param task
	 *            Dumb task containing the values from the HTML form.
	 * @return List of objects that match the form.
	 */
	public static List<Task> search(Task task) {
		String label = "";
		if (task.label != null) label = task.label;
		return Task.coll.find()
				.regex("label", Pattern.compile(".*" + label + ".*")).toArray();

	}

	/**
	 * This search creates a BasicDBObject with exactly the same field there
	 * were provide through the HTML form. Then, the object with matched with
	 * the collection. The values must match exactly.
	 * 
	 * @param task
	 *            Dumb task containing the values from the HTML form.
	 * @return List of objects that exactly match the form.
	 */
	public static List<Task> searchByObject(Task task) {
		String label = task.label;

		BasicDBObject ref = new BasicDBObject();
		if (label != null && !label.isEmpty())
			ref.append("label", label);

		return Task.coll.find(ref).toArray();
	}
}