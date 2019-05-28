package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TrophyRepository;

import domain.Couple;
import domain.Trophy;

@Service
@Transactional
public class TrophyService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private TrophyRepository trophyRepository;

	// ------------------------------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private CoupleService coupleService;

	@Autowired
	private Validator validator;

	// Simple CRUD Methods

	public Trophy create() {
		Trophy result;
		new Trophy();

		result = new Trophy();
		return result;
	}

	public Trophy findOne(final int trophyId) {
		Trophy result;

		result = this.trophyRepository.findOne(trophyId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Trophy> findAll() {
		Collection<Trophy> result;

		result = this.trophyRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Trophy save(final Trophy trophy) {
		Trophy result;

		result = this.trophyRepository.save(trophy);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Trophy trophy) {

		Collection<Couple> couples = this.coupleService.findByTrophyId(trophy
				.getId());

		Assert.notNull(trophy);

		for (final Couple c : couples) {
			Collection<Trophy> trohpies = c.getTrophies();
			trohpies.remove(trophy);
			c.setTrophies(trohpies);
		}
		this.trophyRepository.delete(trophy);
	}

	public Trophy reconstruct(final Trophy trophy, final BindingResult binding) {
		Trophy result;

		if (trophy.getId() == 0) {
			result = trophy;
		} else {
			result = this.trophyRepository.findOne(trophy.getId());
		}
		if (!trophy.getTitle().equals("")) {
			result.setTitle(trophy.getTitle());
		}
		if (!trophy.getPicture().equals("")) {
			result.setPicture(trophy.getPicture());
		}
		if (!(trophy.getScoreToReach() == null)
				&& !(trophy.getScoreToReach() < 0)) {
			result.setScoreToReach(trophy.getScoreToReach());
		} else {
			binding.rejectValue("scoreToReach",
					"trophy.validation.scoreToReach",
					"Score to reach must be a positive number");
		}
		if (!(trophy.getChallengesToComplete() == null)
				&& !(trophy.getChallengesToComplete() < 0)) {
			result.setChallengesToComplete(trophy.getChallengesToComplete());
		} else {
			binding.rejectValue("challengesToComplete",
					"trophy.validation.challengesToComplete",
					"Challenges to complete must be a positive number");
		}
		if (!(trophy.getExperiencesToShare() == null)
				&& !(trophy.getExperiencesToShare() < 0)) {
			result.setExperiencesToShare(trophy.getExperiencesToShare());
		} else {
			binding.rejectValue("experiencesToShare",
					"trophy.validation.experiencesToShare",
					"Experiences to share must be a positive number");
		}
		this.validator.validate(result, binding);

		return result;
	}

}
