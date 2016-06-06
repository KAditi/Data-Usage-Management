package BLL.Implementation;

import BLL.AdminBLL;
import DAL.AdminDAO;
import DAL.Implementation.AdminDAOImpl;


public class AdminBLLImpl implements AdminBLL{
	
	AdminDAO adminDao = new AdminDAOImpl();
	
	public String ValidateEmail(String email)
	{
		String user = adminDao.findAdminByEmail(email);
		//System.out.println("user name email = "+user);
		
		if (user != null) {
			if((user.toString().equalsIgnoreCase(email.toString())))		{	
				return "true";

			} else
				return "false";
		} else 
			return "false";
	}
	
	public String adminsignup(String first_name, String last_name, String email, String password, String phone_number) 
	{
		String rtn = adminDao.adminsignup(first_name, last_name, email, password, phone_number);
		return rtn;
	}
	
		
	
	public String ValidatePhone(String phone_number){
		String user = adminDao.findAdminByPhone(phone_number);
		//System.out.println("user name phno = "+user);
		
		if (user != null) {
			if ((user.toString().equalsIgnoreCase(phone_number.toString()))) {
				return "true";

			} else
				return "false";
		} else 
			return "false";
		
	}
}
