
package forms;

import java.util.Collection;
import java.util.Date;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Couple;
import domain.Experience;
import domain.Feature;

public class BookForm {

	private Date	moment;
	private Date	date;


	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	private Couple		couple;
	private Experience	experience;
	private int			id;
	private Collection<Feature> features;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}
	
	@Valid
	@ManyToOne(optional = false)
	public Couple getCouple() {
		return this.couple;
	}

	public void setCouple(final Couple couple) {
		this.couple = couple;
	}

	@Valid
	@ManyToOne(optional = false)
	public Experience getExperience() {
		return this.experience;
	}

	public void setExperience(final Experience experience) {
		this.experience = experience;
	}
	
	@Valid
	@ManyToMany
	public Collection<Feature> getFeatures() {
		return this.features;
	}

	public void setFeatures(final Collection<Feature> features) {
		this.features = features;
	}

}
