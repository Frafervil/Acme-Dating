
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChallengeRepository;
import domain.Challenge;
import domain.Couple;
import domain.User;

@Service
@Transactional
public class ChallengeService {

	// Managed Repository

	@Autowired
	private ChallengeRepository	challengeRepository;

	// Supporting services

	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods

	public Challenge create() {
		Challenge result;
		User sender;
		User recipient = null;
		Date moment;
		Couple couple;
		Collection<User> users;

		sender = this.userService.findByPrincipal();
		Assert.notNull(sender);

		result = new Challenge();
		Assert.notNull(result);

		moment = new Date(System.currentTimeMillis() - 1);
		Assert.notNull(moment);

		Assert.notNull(sender.getCouple());

		couple = sender.getCouple();
		users = this.userService.findByCoupleId(couple.getId());
		for (final User u : users)
			if (u.getId() != sender.getId()) {
				recipient = u;
				break;
			}
		Assert.notNull(recipient);

		result.setMoment(moment);
		result.setSender(sender);
		result.setRecipient(recipient);
		result.setScore(0);
		result.setStatus("PENDING");

		return result;
	}

	public Challenge save(final Challenge c) {

		Challenge result;
		User principal;
		Date moment;

		Assert.notNull(c);

		principal = this.userService.findByPrincipal();
		Assert.isTrue(c.getSender().getId() == principal.getId());

		moment = new Date(System.currentTimeMillis() - 1);
		Assert.notNull(moment);
		c.setMoment(moment);

		result = this.challengeRepository.save(c);

		return result;
	}

	public void reject(final Challenge c) {
		User principal;

		Assert.notNull(c);
		Assert.isTrue(c.getId() != 0);

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(c.getRecipient().getId() == principal.getId());
		Assert.isTrue(c.getStatus().equals("PENDING"));

		this.challengeRepository.delete(c);
	}

	public void accept(final Challenge c) {
		User principal;

		Assert.notNull(c);
		Assert.isTrue(c.getId() != 0);

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(c.getStatus().equals("PENDING"));
		Assert.isTrue(principal.getId() == c.getRecipient().getId());

		c.setStatus("ACCEPTED");

		this.challengeRepository.save(c);
	}

	public void complete(final Challenge c) {
		User principal;

		Assert.notNull(c);
		Assert.isTrue(c.getId() != 0);

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(c.getStatus().equals("ACCEPTED"));
		Assert.isTrue(principal.getId() == c.getRecipient().getId());

		c.setStatus("COMPLETED");

		this.challengeRepository.save(c);
	}

	public Challenge findOne(final int cRId) {
		Challenge result;

		result = this.challengeRepository.findOne(cRId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Challenge> findAll() {
		Collection<Challenge> result;

		result = this.challengeRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Collection<Challenge> findByRecipientId(final int userId) {
		Collection<Challenge> result;

		result = this.challengeRepository.findByRecipientId(userId);

		return result;
	}

	public Collection<Challenge> findBySenderId(final int userId) {
		Collection<Challenge> result;

		result = this.challengeRepository.findBySenderId(userId);

		return result;
	}

	public Challenge reconstruct(final Challenge c, final BindingResult binding) {
		Challenge result;
		if (c.getId() == 0)
			result = c;
		else
			result = this.challengeRepository.findOne(c.getId());

		result.setSender(this.userService.findByPrincipal());
		result.setMoment(c.getMoment());
		result.setStatus(c.getStatus());
		result.setRecipient(c.getRecipient());
		result.setDescription(c.getDescription());
		result.setEndDate(c.getEndDate());
		result.setScore(c.getScore());
		result.setTitle(c.getTitle());

		this.validator.validate(result, binding);

		return result;
	}
}
