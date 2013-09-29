package controllers;

import play.mvc.*;
import play.data.*;
import models.*;

/**
 * Main controller of this application.
 * 
 * @author Rafael
 * 
 */
public class Application extends Controller {

	/**
	 * Keeps the content obtained from UI HTML forms.
	 */
	static Form<Task> taskForm = Form.form(Task.class);

	/**
	 * Function called when the initial page is invoked.
	 * 
	 * @return Redirection to the task page.
	 */
	public static Result index() {
		// return ok(index.render("Testing without recompiling?"));
		return redirect(routes.Application.tasks());
	}

	/**
	 * Function called when the task page is invoked.
	 * 
	 * @return Task page containing all tasks.
	 */
	public static Result tasks() {
		return ok(views.html.index.render(Task.all(), taskForm));
	}

	/**
	 * Function called when a new task is added through the HTML UI.
	 * 
	 * @return Task page containing all tasks after the addition of the new
	 *         task.
	 */
	public static Result newTask() {
		Form<Task> filledForm = taskForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(Task.all(), filledForm));
		} else {
			Task.create(filledForm.get());
			return redirect(routes.Application.tasks());
		}
	}

	/**
	 * Function called when a task is deleted from the HTML UI.
	 * 
	 * @param id
	 *            Id of the task to be deleted.
	 * @return Task page containing all tasks after the deletion of the given
	 *         task.
	 */
	public static Result deleteTask(String id) {
		Task.delete(id);
		return redirect(routes.Application.tasks());
	}

	/**
	 * Function called from the search page of the HTML UI.
	 * 
	 * @return List of tasks that match the search form from the HTML UI.
	 */
	public static Result search() {
		Form<Task> filledForm = taskForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.search.render(Task.all(), filledForm));
		} else {
			// return
			// ok(views.html.search.render(Task.searchByObject(filledForm.get()),
			// filledForm));
			return ok(views.html.search.render(Task.search(filledForm.get()),
					filledForm));
		}
	}
}