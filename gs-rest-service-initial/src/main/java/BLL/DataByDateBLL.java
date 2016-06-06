package BLL;

import java.util.ArrayList;
import java.util.List;

public interface DataByDateBLL {
	public Long calcDataByDate(String fromdate, String todate);
	
	/* for summary */
	public Long calcDataSummary();
	
	/* for graph */
	public ArrayList calcDataGraph(String frommonth, String phno);	
	
	/* For user list with total data*/
	
	/*Arpita change start*/
	
	public List gettotaldata(String phonenumber);
	
	/*Arpita change end*/
}
