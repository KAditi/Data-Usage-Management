package BLL;

import java.util.List;

import jpa.eclipselink.entity.User;

public interface UserService {
	//Change 5/21/15
	public String ValidateUser(String email,String password, String phone_number);
	// Change ends
	
	public String InsertUser(User user);
	public String UpdateUser(User user);
	public List<User> getusers();
	public String DeleteUser(String email,String phone_number);
}
