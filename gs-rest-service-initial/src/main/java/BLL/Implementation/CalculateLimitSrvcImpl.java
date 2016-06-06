package BLL.Implementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.eclipselink.entity.User;
import BLL.CalculateLimitSrvc;
import DAL.DatabyDateDAO;
import DAL.UserDAO;
import DAL.Implementation.DataByDateDAOImpl;
import DAL.Implementation.UserDAOImpl;

public class CalculateLimitSrvcImpl implements CalculateLimitSrvc {
	DatabyDateDAO dataDao = new DataByDateDAOImpl();
	UserDAO userDao = new UserDAOImpl();
	
	int fday, fmonth, fyear,tday, tmonth, tyear,maxday;
	String fdate, tdate;
	
	/* Get start day from Billing cycle */
	public int func_billday()
	{
		int billday = 0;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();
	
		em.getTransaction().begin(); 	
	 	Query q = em.createNamedQuery("Get Cycle");
		System.out.println("query q "+q);							
		em.getTransaction().commit(); 
		
		billday = Integer.parseInt((String) q.getSingleResult());
		return billday;
	}

	public void get_from_to_date()
	{
		/* --------------------- find current year ----------------------*/		
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year =dateFormat.format(date) ;
		fyear = Integer.parseInt(year);
		
		/* --------------------- find current month ----------------------*/		
		DateFormat dateFormat1 = new SimpleDateFormat("MM");
		Date date1 = new Date();
		String fm =dateFormat1.format(date1) ;
		fmonth = Integer.parseInt(fm);
		
		/* --------------- Get start day from Billing cycle ------------ */		
		fday = func_billday();
		System.out.println("fday = "+fday);
		
		/* ------------------------- find today, tomonth and toyear -------------------- */
		if (fday == 1)
		{
			tday = maxday;
			tmonth = fmonth;
		}
		else
		{
			tday = fday-1;
			if (fmonth==12)
			{
				tmonth=1;
				tyear = fyear+1;
			}
			else
			{
				tmonth=fmonth+1;
				tyear = fyear;
			}
		}
		
		fdate = fyear + "-" + fmonth + "-" + fday;
		tdate = tyear + "-" + tmonth + "-" + tday;
	}


	public String CheckLimit(String phone_number) {
		Long data = null;
		get_from_to_date();
		
		data = dataDao.GetDataByDate(fdate, tdate, phone_number);
		System.out.println("data:" + data);
		
		System.out.println("data:" + fdate);
		System.out.println("data:" + tdate);
		
		//data = dataDao.GetDataByDate("2015-01-01", "2015-12-31", phone_number);

		List<User> r = userDao.findLimitsByPhone(phone_number);

		System.out.println("quota:" + r.get(0).getQuota_val());
		System.out.println("threshold:" + r.get(0).getThreshold_val());

		if (data >= r.get(0).getQuota_val()) {
			return "quota";
		} else if (data >= r.get(0).getThreshold_val()) {
			return "threshold";
		} else
			return "false";
	}

}
