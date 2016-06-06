package BLL.Implementation;

import BLL.BillBLL;
import DAL.BillDAO;
import DAL.Implementation.BillDAOImpl;

public class BillBLLImpl implements BillBLL {
	
	BillDAO bdao = new BillDAOImpl();
	public String checkbillcycle(String fromday)
	{
		String bill = bdao.selectcycle(fromday);
		String billupdate;
		String billinsert;
		
		System.out.println("bill = "+bill);
		if (bill != null) 
		{		
			System.out.println("update bill");
			billupdate = bdao.updatecycle(fromday);			
			return billupdate;
		}
		else
		{
			System.out.println("insert bill");
			billinsert = bdao.insertcycle(fromday);
			return billinsert;
		}
	}
}
