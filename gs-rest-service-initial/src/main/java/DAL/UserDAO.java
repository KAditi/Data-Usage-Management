package DAL;
import java.util.List;

import jpa.eclipselink.entity.User;

public interface UserDAO {
	public User findByUserEmail(String email);
	public String InsertUser(User user);
	public List<User> getuserDetails();
	public String UpdateUser(User user);
	public String DeleteUser(String email,String phone_number);
	public List<User> findLimitsByPhone(String phone_number);
}
