package jpa.eclipselink.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


public class DGraph {
	@Id
	private String first_name;
	private String phoneNumber;
	private Long totaldata;
	
	public DGraph(String first_name, String phoneNumber, Long totaldata) {
		super();
		this.first_name = first_name;
		this.phoneNumber = phoneNumber;
		this.totaldata = totaldata;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getTotaldata() {
		return totaldata;
	}
	public void setTotaldata(Long totaldata) {
		this.totaldata = totaldata;
	}	
}
