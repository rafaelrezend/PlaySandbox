/**
 * 
 */
package models;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.mongojack.Id;
import org.mongojack.JacksonDBCollection;
import org.mongojack.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;

import play.modules.mongojack.MongoDB;

/**
 * This class represents the User entity.
 * User objects are stored to the MongoDB using mongojack.
 * User attributes are annotated with JsonProperty to shorten their names and save storage space.
 * 
 * @author Rafael Rezende (rafaelrezend@gmail.com)
 *
 */
public class User {

	@Id
	public String email;
	@JsonProperty("n")
	public String name;
	@JsonProperty("p")
	public String password;
	@JsonProperty("t")
	// public List<ObjectId> taskList;
	
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
	 * Adds a user to collection. It forces the password to be encrypted.
	 * 
	 * @param user
	 *            User to be added to collection.
	 */
	public static void create(User user) {
		// takes the input non-encrypted password and forces the encryption
		// before saving the user to database.
		user.password = encryptPassword(user.password);

		// note: save method = insert + update methods.
		User.userColl.save(user);
	}

	/**
	 * Deletes a user from collection according to the user email.
	 * 
	 * @param email
	 *            E-mail of the user to be deleted.
	 */
	public static void delete(String email) {
		User user = User.search(email);
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
	public static String encryptPassword(String password) {
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
	public static boolean checkPassword(String attemptPassword, String email) {
		// get the user object using the email reference
		User user = User.search(email);
		if (user == null)
			return false;
		// check if the attempted password is valid
		if (attemptPassword == null)
			return false;
		
		// return the boolean match between the stored and the attempted passwords
		return BCrypt.checkpw(attemptPassword, user.password);
	}

}
