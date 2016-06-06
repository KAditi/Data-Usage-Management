package BLL;

public interface AdminBLL {
	public String adminsignup(String first_name, String last_name, String email, String password, String phone_number) ;
	public String ValidateEmail(String email);
	public String ValidatePhone(String phone_number);
}
