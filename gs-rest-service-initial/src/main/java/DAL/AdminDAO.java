package DAL;

public interface AdminDAO {
	public String adminsignup(String first_name, String last_name, String email, String password, String phone_number);
	public String findAdminByEmail(String email);
	public String findAdminByPhone(String phone_number);
}
