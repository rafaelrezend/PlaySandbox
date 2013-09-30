/**
 * 
 */
package models;

import java.util.List;

import org.mongojack.Id;
import org.mongojack.JacksonDBCollection;

import play.modules.mongojack.MongoDB;

/**
 * @author Rafael
 * 
 */
public class User {

	@Id
	public String email;
	public String name;
	public String password;

	/**
	 * MongoDB Jackson representation for collection "users".
	 */
	private static JacksonDBCollection<User, String> userColl = MongoDB
			.getCollection("users", User.class, String.class);

	/**
	 * Lists all users in the collection.
	 * 
	 * @return list containing all users.
	 */
	public static List<User> all() {
		return User.userColl.find().toArray();
	}
	
	/**
	 * Adds a user to collection.
	 * 
	 * @param user
	 *            User to be added to collection.
	 */
	public static void create(User user) {
		User.userColl.save(user);
	}

}
