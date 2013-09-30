/**
 * 
 */
package models;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
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

	public void setPassword(String password) {
		if (password == null) {
			System.out.println("Password NULL!!");
			this.password = "";
		} else
			this.password = encryptPassword(password);
	}

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

	/**
	 * Deletes a user from collection according to the user email.
	 * 
	 * @param email
	 *            E-mail of the user to be deleted.
	 */
	public static void delete(String email) {
		User user = User.userColl.findOneById(email);
		if (user != null)
			// This method looks for the @ObjectId element. A query is required
			// for removing based on other fields.
			User.userColl.removeById(email);
	}

	/**
	 * Searches the user by email.
	 * 
	 * @param email
	 *            E-mail of the desired user.
	 * @return User corresponding to the given e-mail, or null if user not
	 *         found.
	 */
	public static User search(String email) {
		User user = null;
		user = User.userColl.findOneById(email);

		return user;
	}

	/**
	 * Encrypts the password passed as parameter.
	 * 
	 * @param password
	 *            Password to be encrypted.
	 * @return The encrypted password.
	 */
	public String encryptPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Check if the user password corresponds to the one in database.
	 * 
	 * @param attemptPassword
	 *            Password attempted via HMTL UI.
	 * @param encryptedPassword
	 *            Encrypted
	 * @return
	 */
	public static boolean checkPassword(String attemptPassword, User user) {
		if (attemptPassword == null) {
			return false;
		}
		return BCrypt.checkpw(attemptPassword, user.password);
	}

}
