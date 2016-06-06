package BLL.Implementation;

import BLL.DataService;
import DAL.DataDAO;
import DAL.Implementation.DataDAOImpl;

public class DataServiceImpl implements DataService {
	
	DataDAO obj = new DataDAOImpl();

	@Override
	public String insertData(String phoneNumber, int dataUsage) {
		// TODO Auto-generated method stub
		String tag = obj.insertData(phoneNumber, dataUsage);
		return tag;
	}

}
