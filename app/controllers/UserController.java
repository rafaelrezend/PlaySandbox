/**
 * 
 */
package controllers;

import models.Task;
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
	 * Function called when a new task is added through the HTML UI.
	 * 
	 * @return Task page containing all tasks after the addition of the new
	 *         task.
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
}
