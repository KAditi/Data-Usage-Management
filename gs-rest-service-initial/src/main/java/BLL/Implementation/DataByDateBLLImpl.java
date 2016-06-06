package BLL.Implementation;

import BLL.DataByDateBLL;
import DAL.DatabyDateDAO;
import DAL.Implementation.DataByDateDAOImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DataByDateBLLImpl implements DataByDateBLL {
	
	DatabyDateDAO datedao = new DataByDateDAOImpl();
	
	@Override
	public Long calcDataByDate(String fromdate, String todate)
	{
		Long tag = datedao.findDataByDate(fromdate, todate); 				
		return tag;		
	}
	
	
	int fday, fmonth, fyear,tday, tmonth, tyear;
	int maxday;
	int count;
		
	String currdate;
	String fdate, tdate;
	
	ArrayList<HashMap<Integer, Long>> arraylist;
		
	/* *************** Function to find month number ************** */
	public int func_findFmonth(String frommonth)
	{
		int fm = 0;
		
		if (frommonth.equals("Jan"))
			fm =1;
		else if (frommonth.equals("Feb"))
			fm =2;
		else if (frommonth.equals("Mar"))
			fm=3;
		else if (frommonth.equals("Apr"))
			fm=4;
		else if (frommonth.equals("May"))
			fm=5;
		else if (frommonth.equals("Jun"))
			fm=6;
		else if (frommonth.equals("Jul"))
			fm=7;
		else if (frommonth.equals("Aug"))
			fm=8;
		else if (frommonth.equals("Sep"))
			fm=9;
		else if (frommonth.equals("Oct"))
			fm=10;
		else if (frommonth.equals("Nov"))
			fm=11;
		else if (frommonth.equals("Dec"))
			fm = 12;	
		
		return fm;
	}
	
	/* ******** Func to find maxday ************ */
	public int func_findmaxday(String frommonth)
	{
		int max = 0;
		if (frommonth.equals("Jan") || frommonth.equals("Mar") || frommonth.equals("May") || frommonth.equals("Jul") || frommonth.equals("Aug") || frommonth.equals("Oct") || frommonth.equals("Dec"))
			max = 31;			
		else if (frommonth.equals("Apr") || frommonth.equals("Jun") || frommonth.equals("Sep") || frommonth.equals("Nov")) 
			max = 30;
		else
			max=28;
		
		return max;
	}
	
	/* ****************** Get start day from Billing cycle ******************/
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
	
	/* ************ For Graph ****************** */
	public ArrayList<HashMap<Integer, Long>> calcDataGraph(String frommonth, String phno)
	{		
		count = 0;
		
		/* -----------------Convert Month to digit ----------- */ 
		fmonth = func_findFmonth(frommonth);
			
		/* ------------ Find max day of month----------------- */
		maxday = func_findmaxday(frommonth);
		
		/* --------------------- find current year ----------------------*/		
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year =dateFormat.format(date) ;
		fyear = Integer.parseInt(year);
				
		/* --------------- Get start day from Billing cycle ------------ */		
		fday = func_billday();
		
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
			
		/* ------------------ Get Data ---------------------*/
		arraylist = new ArrayList<HashMap<Integer,Long>>();
		if (fmonth==tmonth)
		{
			/* if same month : 1-Jan to 31-Jan */
			for (int i=fday; i<= tday ; i++)
			{
				HashMap<Integer, Long> map = new HashMap<Integer, Long>();
				
				currdate = fyear + "-" + fmonth + "-" + i;
				//System.out.println("1 - currdate"+ currdate);
				Long tag = datedao.findDataGraph(currdate, phno);
				count++;
				if(tag!=null)
					map.put(count,tag);
				else
					map.put(count, (long)0);
				arraylist.add(map);
			}
		}
		else
		{		
			/*  Diff month 10-Jan to 9-Feb*/
			
			/* than loop for 10-Jan to 31-Jan */
			for (int i=fday; i<= maxday; i++)
			{
				HashMap<Integer, Long> map = new HashMap<Integer, Long>();
				
				currdate = fyear + "-" + fmonth + "-" + i;
				//System.out.println("2 - currdate"+ currdate);
				Long tag = datedao.findDataGraph(currdate, phno);
				count++;
				
				if(tag!=null)
					map.put(count,tag);
				else
					map.put(count, (long)0);				
				arraylist.add(map);
			}
			
			/* than loop for 1-Feb to 9-Feb */
			for(int i=1 ; i<=tday; i++)
			{
				HashMap<Integer, Long> map = new HashMap<Integer, Long>();
				
				currdate = tyear + "-" + tmonth + "-" + i;
				Long tag = datedao.findDataGraph(currdate, phno);
				count++;				
				if(tag!=null)
					map.put(count,tag);
				else
					map.put(count, (long)0);
				arraylist.add(map);
			}
		}		
		return arraylist;	
	}
	
	/* ************************* Data Summery **************************** */
	
	@Override
	public Long calcDataSummary()
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
		
		System.out.println("fdate = "+ fdate);
		System.out.println("tdate = "+ tdate);
		
		Long tag = datedao.findDataByDate(fdate, tdate);
		System.out.println("total data"+tag);
		return tag;		
	}
	
	/*Arpita change start*/
	
	public List gettotaldata(String phonenumber)
	{
		return datedao.getUserTotaldata(phonenumber);
	}
	/*Arpita change end*/
}
