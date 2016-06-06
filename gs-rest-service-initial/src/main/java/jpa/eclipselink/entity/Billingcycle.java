package jpa.eclipselink.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table (name="billingcycle")
@NamedQueries({
	@NamedQuery(query = "select u.fromday from Billingcycle u", name = "Get Cycle"),
	@NamedQuery(query = "Update Billingcycle b set b.fromday = :fromday", name = "Update Cycle"),
	@NamedQuery(query = "select u from Billingcycle u where u.fromday = :fromday", name = "Get Cycle fday"),
})

public class Billingcycle {

	@Id
	private String fromday;

	public String getFromday() {
		return fromday;
	}

	public void setFromday(String fromday) {
		this.fromday = fromday;
	}
	
}
