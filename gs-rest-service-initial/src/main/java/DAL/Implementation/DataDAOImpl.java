package DAL.Implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import DAL.DataDAO;
import jpa.eclipselink.entity.Data;

public class DataDAOImpl implements DataDAO {

	@Override
	public String insertData(String phoneNumber, int dataUsage) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		Data dataObj = new Data();
		
		dataObj.setPhoneNumber(phoneNumber);
		dataObj.setDataUsage(dataUsage);
		dataObj.setCurrDate();
		dataObj.setCurrTime();
		
		em.persist(dataObj);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		
		return "true";
		
		
		
		
	}

}
