package jpa.eclipselink.entity;

//Change by Aneri
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table
@NamedQueries({
		@NamedQuery(query = "Select u.phone_number from User u where u.phone_number = :phone_number", name = "find user by phone_number"),
		@NamedQuery(query = "Select u.email from User u where u.email = :email", name = "find user by email"),
		@NamedQuery(query = "Select u from User u", name = "Get Users"),
		/*Arpita change start*/
		@NamedQuery(query = "Select u.priority from User u where u.phone_number = :phoneNumber",name = "find priority"),
		/*Arpita change end*/
		// Changed by Aneri
		@NamedQuery(query = "Select u from User u where u.phone_number= :phone_number", name = "Get data limits")})
public class User {
	
	private String first_name;
	private String last_name;
	@Id
	private String email;
	private String password;
	private String phone_number;
	private int priority;
	private int quota;
	private int quota_val;
	private int threshold;
	private int threshold_val;
	private String data_flag;
	private int is_delete;
	private int is_valid;
	private int data_limit;

	public User(String first_name, String last_name, String email,
			String password, String phone_number, int priority, int quota,
			int quota_val, int threshold, int threshold_val, String data_flag,
			int is_delete, int is_valid, int data_limit) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.phone_number = phone_number;
		this.priority = priority;
		this.quota = quota;
		this.quota_val = quota_val;
		this.threshold = threshold;
		this.threshold_val = threshold_val;
		this.data_flag = data_flag;
		this.is_delete = is_delete;
		this.is_valid = is_valid;
		this.data_limit = data_limit;
	}

	public int getQuota_val() {
		return quota_val;
	}

	public void setQuota_val(int quota_val) {
		this.quota_val = quota_val;
	}

	public int getThreshold_val() {
		return threshold_val;
	}

	public void setThreshold_val(int threshold_val) {
		this.threshold_val = threshold_val;
	}

	public int getData_limit() {
		return data_limit;
	}

	public void setData_limit(int data_limit) {
		this.data_limit = data_limit;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getData_flag() {
		return data_flag;
	}

	public void setData_flag(String data_flag) {
		this.data_flag = data_flag;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public User() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [first_name=" + first_name + ", last_name=" + last_name
				+ ", email=" + email + ", password=" + password
				+ ", phone_number=" + phone_number + ", priority=" + priority
				+ ", quota=" + quota + ", quota_val=" + quota_val
				+ ", threshold=" + threshold + ", threshold_val="
				+ threshold_val + ", data_flag=" + data_flag + ", is_delete="
				+ is_delete + ", is_valid=" + is_valid + ", data_limit="
				+ data_limit + "]";
	}
}
