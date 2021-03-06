package controllers.any;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.CustomisationService;
import services.ExperienceCommentService;
import services.ExperienceService;
import services.UserService;
import controllers.AbstractController;
import domain.Customisation;
import domain.Experience;
import domain.ExperienceComment;
import domain.User;

@Controller
@RequestMapping("/experience")
public class ExperienceController extends AbstractController {

	@Autowired
	private ExperienceService experienceService;

	@Autowired
	private ExperienceCommentService experienceCommentService;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CustomisationService customisationService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(required = false) final String keyword,
			@RequestParam(required = false, defaultValue = "false") final Boolean keywordBool) {
		final ModelAndView result;
		Collection<Experience> experiences;
		experiences = new ArrayList<Experience>();

		if (keywordBool && keyword != null)
			experiences = this.experienceService.findByKeywordAll(keyword);
		else
			experiences = this.experienceService.findAll();

		result = new ModelAndView("experience/list");
		result.addObject("experiences", experiences);
		result.addObject("requestURI", "experience/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int experienceId) {
		// Inicializa resultado
		final ModelAndView result;
		Experience experience;
		Collection<ExperienceComment> comments;
		Collection<ExperienceComment> commentsChild = new ArrayList<ExperienceComment>();

		User user;
		boolean hasCouple = false;
		Customisation customisation;
		Double vat;
		customisation = this.customisationService.find();
		Double vatNumber = customisation.getVatNumber();
		if(vatNumber == null){
			vatNumber = 0.0;
		}
		vat = (vatNumber) / 100 + 1.0;

		// Busca en el repositorio
		experience = this.experienceService.findOne(experienceId);
		Assert.notNull(experience);

		comments = this.experienceCommentService
				.findByExperienceId(experienceId);
		
		for (ExperienceComment eC : comments) {
			commentsChild.addAll(this.experienceCommentService.findChilds(eC.getId()));
		}
		try {
			user = this.userService.findByPrincipal();
			if (user.getCouple() != null) {
				hasCouple = true;
			}
		} catch (Exception e) {
		}

		// Crea y a�ade objetos a la vista
		result = new ModelAndView("experience/display");
		result.addObject("requestURI", "experience/display.do");
		result.addObject("experience", experience);
		result.addObject("hasCouple", hasCouple);
		result.addObject("comments", comments);
		result.addObject("commentsChild", commentsChild);
		result.addObject("vat", vat);

		// Env�a la vista
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "computeScore")
	public ModelAndView computeScore() {
		final ModelAndView result;

		this.companyService.computeScore();

		result = new ModelAndView("redirect:list.do");

		return result;
	}

}
