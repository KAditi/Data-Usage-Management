package jpa.eclipselink.entity;

import java.util.List;

public class UserData {
	private List<User> lst;

	public List<User> getLst() {
		return lst;
	}

	public UserData(List<User> lst) {
		this.lst = lst;
	}
}
