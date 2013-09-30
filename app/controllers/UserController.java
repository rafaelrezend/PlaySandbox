/**
 * 
 */
package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author Rafael
 *
 */
public class UserController extends Controller {
	
	/**
	 * Keeps the content obtained from UI HTML forms.
	 */
	static Form<User> userForm = Form.form(User.class);
	
	/**
	 * Function called when the task page is invoked.
	 * 
	 * @return Task page containing all tasks.
	 */
	public static Result users() {
		return ok(views.html.user.render(User.all(), userForm));
	}

	/**
	 * Function called when a new user is added through the HTML UI.
	 * 
	 * @return Users page containing all users after the addition of the new
	 *         user.
	 */
	public static Result newUser() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.user.render(User.all(), filledForm));
		} else {
			User.create(filledForm.get());
			return redirect(routes.UserController.users());
		}
	}
	
	/**
	 * Function called when a user is deleted from the HTML UI.
	 * 
	 * @param email
	 *            E-mail of the user to be deleted.
	 * @return Users page containing all users after the deletion of the given
	 *         user.
	 */
	public static Result deleteUser(String email) {
		User.delete(email);
		return redirect(routes.UserController.users());
	}
	
	/**
	 * Function called from the search page of the HTML UI.
	 * 
	 * @return Show the user details that match the search form from the HTML UI.
	 */
	public static Result search() {
		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.search_user.render(null, filledForm));
		} else {
			// return
			// ok(views.html.search.render(Task.searchByObject(filledForm.get()),
			// filledForm));
			User user = filledForm.get();
			user = User.search(user.email);
			
			if (user == null)
				return ok(views.html.search_user.render(user, filledForm));
			return ok(views.html.search_user.render(user, filledForm));
		}
	}
}
