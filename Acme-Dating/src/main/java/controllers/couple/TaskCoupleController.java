package controllers.couple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CoupleService;
import services.TaskService;
import services.UserService;
import controllers.AbstractController;
import domain.Couple;
import domain.Task;
import domain.User;

@Controller
@RequestMapping("/task/couple")
public class TaskCoupleController extends AbstractController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private CoupleService coupleService;

	@Autowired
	private UserService userService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Task> tasks;
		tasks = new ArrayList<Task>();
		result = new ModelAndView("task/list");
		result.addObject("requestURI", "task/couple/list.do");

		final User principal = this.userService.findByPrincipal();

		if (principal.getCouple() == null) {
			result.addObject("couple", null);
			return result;
		} else {

			final Couple couple = this.coupleService.findByUser();
			tasks = this.taskService.findByCoupleId(couple.getId());

			result.addObject("tasks", tasks);
			result.addObject("couple", couple);
			return result;
		}
	}

	// Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Task task;

		task = this.taskService.create();
		result = this.createModelAndView(task);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int taskId) {
		ModelAndView result;
		Task task;

		task = this.taskService.findOne(taskId);
		Assert.notNull(task);
		result = this.createEditModelAndView(task);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("task") Task task,
			final BindingResult binding) {
		ModelAndView result;

		try {
			task = this.taskService.reconstruct(task, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(task);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else {
				task = this.taskService.save(task);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(task, "task.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("task") Task task,
			final BindingResult binding) {
		ModelAndView result;

		try {
			task = this.taskService.reconstruct(task, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(task);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else {
				task = this.taskService.save(task);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(task, "task.commit.error");
		}
		return result;
	}

	// Delete

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int taskId) {
		ModelAndView result;
		Task task;

		task = this.taskService.findOne(taskId);

		try {
			this.taskService.delete(task);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(task, "task.commit.error");
		}
		return result;
	}

	// ------------------- Ancillary Methods

	protected ModelAndView createEditModelAndView(final Task task) {
		ModelAndView result;

		result = this.createEditModelAndView(task, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Task task,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("task/edit");
		result.addObject("task", task);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createModelAndView(final Task task) {
		ModelAndView result;

		result = this.createModelAndView(task, null);
		return result;
	}

	private ModelAndView createModelAndView(final Task task,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("task/create");
		result.addObject("task", task);
		result.addObject("message", messageCode);
		return result;
	}

}
