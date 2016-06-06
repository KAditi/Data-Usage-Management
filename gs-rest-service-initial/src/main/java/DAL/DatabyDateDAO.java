package DAL;

import java.util.Date;
import java.util.List;

public interface DatabyDateDAO {
	public Long findDataByDate(String fromdate, String todate);
	public Long GetDataByDate(String fromdate, String todate,String phone_number);
	
	/* for graph array*/
	public Long findDataGraph(String fromdate, String phno);
	
	/*Arpita change start*/
	
	public List getUserTotaldata(String phonenumber);
	
	/*Arpita change end*/
}
