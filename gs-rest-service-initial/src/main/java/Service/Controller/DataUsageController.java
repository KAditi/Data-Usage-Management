package Service.Controller;

import java.util.List;

import jpa.eclipselink.entity.UserAuthentic;
import jpa.eclipselink.entity.User;
import jpa.eclipselink.entity.UserData;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import BLL.AdminBLL;
import BLL.BillBLL;
import BLL.CalculateLimitSrvc;
import BLL.DataByDateBLL;
import BLL.DataService;
import BLL.UserService;
import BLL.Implementation.AdminBLLImpl;
import BLL.Implementation.BillBLLImpl;
import BLL.Implementation.CalculateLimitSrvcImpl;
import BLL.Implementation.DataByDateBLLImpl;
import BLL.Implementation.DataServiceImpl;
import BLL.Implementation.UserServiceImpl;

@RestController
public class DataUsageController {

	String tag = "";
	String tag1 = "";
	String tag2 = "";
	String tag4 = "";
	DataService dataservObj = new DataServiceImpl();

	UserService usrsrvc = new UserServiceImpl();
	AdminBLL abll = new AdminBLLImpl();
	BillBLL bbll = new BillBLLImpl();
	DataByDateBLL datebll = new DataByDateBLLImpl();
	CalculateLimitSrvc calLimSrvc = new CalculateLimitSrvcImpl();

	//Change 5/21/15
	@RequestMapping("/login")
	public UserAuthentic greeting(@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value="phone_number") String phone_number) {
		tag = usrsrvc.ValidateUser(email, password,phone_number);

		return new UserAuthentic(tag);
	}
	//Change ends

	@RequestMapping("/add_account")
	public @ResponseBody
	UserAuthentic update(@RequestParam("first_name") String first_name,
			@RequestParam(value = "last_name") String last_name,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "phone_number") String phone_number,
			@RequestParam(value = "priority") String priority,
			@RequestParam(value = "quota") String quota,
			@RequestParam(value = "threshold") String threshold,
			@RequestParam(value = "data_flag") String data_flag,
			@RequestParam(value = "is_delete") String is_delete,
			@RequestParam(value = "is_valid") String is_valid) {
		User user = new User(first_name, last_name, email, password,
				phone_number, Integer.parseInt(priority),
				Integer.parseInt(quota),
				((Integer.parseInt(quota) * 2 * 1024 * 1024) / 100),
				Integer.parseInt(threshold),
				((Integer.parseInt(threshold) * 2 * 1024 * 1024) / 100),
				data_flag, Integer.parseInt(is_delete),
				Integer.parseInt(is_valid), 2);

		tag1 = abll.ValidateEmail(email);
		tag2 = abll.ValidatePhone(phone_number);

		System.out.println("tag1 email =" + tag1);
		System.out.println("tag2 phno =" + tag2);

		if ((tag1 != "true") && (tag2 != "true") && (user != null))
			tag = usrsrvc.InsertUser(user);
		else
			tag = "false";

		// TODO: call persistence layer to update
		return new UserAuthentic(tag);
	}

	@RequestMapping("/edit_account")
	public @ResponseBody
	UserAuthentic edit(@RequestParam("first_name") String first_name,
			@RequestParam(value = "last_name") String last_name,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "phone_number") String phone_number,
			@RequestParam(value = "priority") String priority,
			@RequestParam(value = "quota") String quota,
			@RequestParam(value = "threshold") String threshold,
			@RequestParam(value = "data_flag") String data_flag,
			@RequestParam(value = "is_delete") String is_delete,
			@RequestParam(value = "is_valid") String is_valid) {
		User user = new User(first_name, last_name, email, password,
				phone_number, Integer.parseInt(priority),
				Integer.parseInt(quota),
				((Integer.parseInt(quota) * 2 * 1024 * 1024) / 100),
				Integer.parseInt(threshold),
				((Integer.parseInt(threshold) * 2 * 1024 * 1024) / 100),
				data_flag, Integer.parseInt(is_delete),
				Integer.parseInt(is_valid), 2);

		tag1 = abll.ValidateEmail(email);
		tag2 = abll.ValidatePhone(phone_number);

		System.out.println("tag1 email =" + tag1);
		System.out.println("tag2 phno =" + tag2);

		if ((tag1 == "true") && (tag2 == "true") && (user != null)) {
			tag = usrsrvc.UpdateUser(user);
			System.out.println("Update user now");
		} else
			tag = "false";

		// TODO: call persistence layer to update
		return new UserAuthentic(tag);
	}

	@RequestMapping("/remove_account")
	public @ResponseBody
	UserAuthentic remove(@RequestParam(value = "email") String email,
			@RequestParam(value = "phone_number") String phone_number) {

		tag = usrsrvc.DeleteUser(email, phone_number);

		// TODO: call persistence layer to update
		return new UserAuthentic(tag);
	}

	@RequestMapping("/signup")
	public UserAuthentic admin1(
			@RequestParam(value = "first_name") String firstname,
			@RequestParam(value = "last_name") String lastname,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "phone_number") String phone_number) {

		tag1 = abll.ValidateEmail(email);
		// System.out.println("HEREEEEEEE tag1");
		tag2 = abll.ValidatePhone(phone_number);

		System.out.println("tag1 email =" + tag1);
		System.out.println("tag2 phno =" + tag2);

		if ((tag1 != "true") && (tag2 != "true"))
			tag = abll.adminsignup(firstname, lastname, email, password,
					phone_number);
		else
			tag = "false";

		return new UserAuthentic(tag);
	}

	@RequestMapping("/getUsers")
	public UserData data1() {
		List l = usrsrvc.getusers();
		System.out.println("list in data = " + l);
		return new UserData(l);
	}

	@RequestMapping("/data")
	public UserAuthentic datausage(
			@RequestParam(value = "phoneNumber") String phoneNumber,
			@RequestParam(value = "dataUsage") int dataUsage) {
		tag4 = dataservObj.insertData(phoneNumber, dataUsage);

		return new UserAuthentic(tag4);
	}

	@RequestMapping("/bill")
	public UserAuthentic bill1(@RequestParam(value = "fromday") String fromday) {
		tag = bbll.checkbillcycle(fromday);
		return new UserAuthentic(fromday);
	}

	@RequestMapping("/dataSummery")
	public UserAuthentic DataSummery() {
		Long r = datebll.calcDataSummary();
		// return r;
		tag4 = Long.toString(r);
		return new UserAuthentic(tag4);
	}

	@RequestMapping("/dateGraph")
	public UserData CalcDataGraph(
			@RequestParam(value = "frommonth") String frommonth,
			@RequestParam(value = "phonenumber") String phonenumber) {
		List arrlist = datebll.calcDataGraph(frommonth, phonenumber);
		return new UserData(arrlist);
	}
	
	/*Arpita change start*/
	@RequestMapping("/User_Totaldata_List")
	public UserData UserwithTotaldata(@RequestParam(value="phonenumber") String phonenumber)
	{
		List  l = datebll.gettotaldata(phonenumber);
		System.out.println(" total data = "+l);
		return new UserData(l);
	}
	/*Arpita change end*/

	// Aneri
	@RequestMapping("/cal_limits")
	public UserAuthentic Cal_Limits(
			@RequestParam(value = "phone_number") String phone_number) {
		String tag = calLimSrvc.CheckLimit(phone_number);
		System.out.println("tag cal_limits= " + tag);
		return new UserAuthentic(tag);
	}
}
