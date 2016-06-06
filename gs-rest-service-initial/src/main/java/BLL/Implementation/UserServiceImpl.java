package BLL.Implementation;

import java.util.List;

import jpa.eclipselink.entity.User;
import BLL.UserService;
import DAL.UserDAO;
import DAL.Implementation.UserDAOImpl;

public class UserServiceImpl implements UserService {
	UserDAO userDao = new UserDAOImpl();
	String response = "";

	//Change 5/21/15
	public String ValidateUser(String email, String password,
			String phone_number) {
		User user = userDao.findByUserEmail(email);

		if (user != null) {
			if ((user.getEmail().toString().equalsIgnoreCase(email.toString()))
					&& (user.getPassword().toString().equalsIgnoreCase(password
							.toString()))
					&& (user.getPhone_number().equalsIgnoreCase(phone_number))) {
				if (user.getPriority() == 2) {
					return "user";
				} else
					return "admin";

			} else
				return "false";
		} else {
			return "false";
		}
	}
	//Change ends
	
	/*
	 * String email, String first_name, String last_name, int phone_number,int
	 * priority, int quota,int threshold,char data_flag
	 */
	public String InsertUser(User user) {

		if (user != null) {
			response = userDao.InsertUser(user);
		}
		return response;
	}

	public List<User> getusers() {

		return userDao.getuserDetails();
	}

	public String UpdateUser(User user) {

		if (user != null) {
			response = userDao.UpdateUser(user);
			// System.out.println("Response in service"+response);
		}
		return response;
	}

	public String DeleteUser(String email, String phone_number) {
		response = userDao.DeleteUser(email, phone_number);
		return response;
	}
}
