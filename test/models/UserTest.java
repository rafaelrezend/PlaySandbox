/**
 * 
 */
package models;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;

import play.test.FakeApplication;
import static play.test.Helpers.*;

/**
 * JUnit test for the class model User.
 * 
 * @author Rafael Rezende (rafaelrezend@gmail.com)
 *
 */
public class UserTest {
	
	// MongoDB address and port. MUST match the application configuration.
	public static final String TEST_DB_ADDR = "localhost";
	public static final int TEST_DB_PORT = 27017;
	
	// Name of the database for tests
	public static final String TEST_DB_NAME = "junit";
	
	private static FakeApplication app;
	
	/**
	 * Executed ONCE before the test starts.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		// Create configuration map
		Map<String, String> config = new HashMap<String, String>();

		// Keep mongoDB connection opened until the tests are finished 
	    config.put("ehcacheplugin", "disabled");
	    config.put("mongodbJacksonMapperCloseOnStop", "disabled");
	    
	    // Write tests to a separate database
	    config.put("mongodb.database", TEST_DB_NAME);
	    app = fakeApplication(config);
	}
	
	/**
	 * Executed after EVERY test.
	 */
	@After
	public void tearDown() {
		// Drop the database used for testing
		try {
			MongoClient mongo = new MongoClient(TEST_DB_ADDR, TEST_DB_PORT);
			mongo.getDB(TEST_DB_NAME).dropDatabase();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates user in the database and checks its retrieval.
	 */
	@Test
	public void createUser() {
		
		running(app, new Runnable() {
			public void run() {

				// Create new user with reference information
				User user = new User("test@user.com", "Test User", "secret", null);

				// Add user to database
				User.create(user);
				// Retrieve a user from its id (email)
				User userDb = User.search("test@user.com");

				// Assert fields are matching
				Assert.assertEquals("test@user.com", userDb.email);
				Assert.assertEquals("Test User", userDb.name);
			}
		});
	}
	
	/**
	 * Checks the password validation.
	 */
	@Test
	public void checkPassword() {
		
		running(app, new Runnable() {
			public void run() {
				
				// Add user to database
				User.create(new User("test@user.com", "Test User", "secret", null));
				
				// Assert password
				Assert.assertTrue(User.checkPassword("secret", "test@user.com"));
				Assert.assertFalse(User.checkPassword("wrongpass", "test@user.com"));
			}
		});
	}
	
	/**
	 * Checks if the user has been properly deleted.
	 */
	@Test
	public void deleteUser() {
		
		running(app, new Runnable() {
			public void run() {
				
				// Add user to database
				User.create(new User("test@user.com", "Test User", "secret", null));
				
				// Check that the user exists in the database
				Assert.assertNotNull(User.search("test@user.com"));
				
				// Delete user from database
				User.delete("test@user.com");
				
				// Confirm that a search for this user returns null
				Assert.assertNull(User.search("test@user.com"));
			}
		});
	}

}
