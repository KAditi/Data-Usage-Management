package jpa.eclipselink.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//Change by Aneri

@Entity
@Table (name="Data")
@NamedQueries({
	@NamedQuery(query = "Select sum(d.dataUsage) from Data d where d.currDate between :fromdate and :todate",name = "find databydate"),
	@NamedQuery(query = "Select sum(d.dataUsage) from Data d where d.currDate = :currdate and d.phoneNumber = :phoneNumber",name = "find datagraph"),
	/*Arpita change start*/
	@NamedQuery(query = "Select u.first_name, d.phoneNumber, sum(d.dataUsage) as totaldata from User as u, Data as d where d.phoneNumber = u.phone_number and d.phoneNumber = :phno group by d.phoneNumber",name = "get User_totaldata"),
	/*Arpita change end*/
	@NamedQuery(query = "Select u.first_name, d.phoneNumber, sum(d.dataUsage) from User as u, Data as d where d.phoneNumber = u.phone_number group by d.phoneNumber",name = "get totaldata")
})

public class Data {
	
	@Id
	private String phoneNumber;
	private int dataUsage; 
	
	//Changed by Aneri
	//@Temporal(TemporalType.DATE) private java.util.Date currDate;
	private String currDate;
	// Change End
	
	private String currTime;
	public String getCurrTime() {
		return currTime;
	}
	public void setCurrTime() {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		this.currTime = dateFormat.format(date) ;
	}
	public String getCurrDate() {
		return currDate;
	}
	
	//Changed by Aneri
	public void setCurrDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		this.currDate =dateFormat.format(date) ;
		//this.currDate =date;
	// Change ends
		System.out.println("currDate"+currDate);
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getDataUsage() {
		return dataUsage;
	}
	public void setDataUsage(int dataUsage) {
		this.dataUsage = dataUsage;
	}
	
	
	
	

}
