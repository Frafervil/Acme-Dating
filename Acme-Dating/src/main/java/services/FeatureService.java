
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.FeatureRepository;
import domain.Company;
import domain.Feature;

@Service
@Transactional
public class FeatureService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private FeatureRepository	featureRepository;
	// Supporting services ----------------------------------------------------
	@Autowired
	private CompanyService		companyService;


	// Simple CRUD Methods

	public Feature create() {
		Feature result;
		final Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		result = new Feature();
		result.setCompany(principal);
		return result;
	}

	public Feature save(final Feature feature) {
		Feature result;
		Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(feature);
		Assert.isTrue(feature.getCompany() == principal);

		result = this.featureRepository.save(feature);
		Assert.notNull(result);
		return result;
	}

	public void delete(final Feature feature) {
		this.featureRepository.delete(feature);

	}

	public Feature findOne(final int featureId) {
		Feature result;

		result = this.featureRepository.findOne(featureId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Feature> findAll() {
		Collection<Feature> result;

		result = this.featureRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Business Methods

	public Collection<Feature> findAllByCompanyId(final int companyId) {
		Collection<Feature> result;

		result = this.featureRepository.findAllByCompanyId(companyId);

		return result;
	}
}
