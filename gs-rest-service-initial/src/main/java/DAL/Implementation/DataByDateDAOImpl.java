package DAL.Implementation;

// Change by Aneri
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import jpa.eclipselink.entity.DGraph;
import jpa.eclipselink.entity.Data;

import com.mysql.jdbc.log.Log;

import DAL.DatabyDateDAO;

public class DataByDateDAOImpl implements DatabyDateDAO {
	
	public Long findDataByDate(String fromdate, String todate) throws NullPointerException, NoResultException
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		try
		{
		 	Query q = em.createNamedQuery("find databydate");
		 	System.out.println("query = "+q);
			q.setParameter("fromdate", fromdate);
			q.setParameter("todate", todate);
			
			
			System.out.println("fromdate = "+fromdate);
			System.out.println("todate = "+todate);
			
			em.getTransaction().commit(); 
			return (Long)q.getSingleResult();		
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}		
	}
	
	public Long findDataGraph(String fromdate, String phno) throws NullPointerException, NoResultException
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		try
		{
		 	Query q = em.createNamedQuery("find datagraph");
		 	System.out.println("query = "+q);
			q.setParameter("currdate", fromdate);
			q.setParameter("phoneNumber", phno);	
			
			System.out.println("query result = "+ q.getSingleResult());
			em.getTransaction().commit(); 
			return (Long)q.getSingleResult();		
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}	
	}
	
	/*public List findDataGraph(String fdate, String ldate, String phno)
	{
		String currdate;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin(); 
	 	Query q = em.createNamedQuery("find datagraph");
	 	System.out.println("query = "+q);
		
	 	q.setParameter("currdate", currdate);
		q.setParameter("phoneNumber", phno);				
		em.getTransaction().commit(); 
	}*/
	
	/*Arpita change start*/
	public List<DGraph> getUserTotaldata(String phonenumber)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		try
		{
			Query qP = em.createNamedQuery("find priority");			
			qP.setParameter("phoneNumber", phonenumber);
			//System.out.println("query prio "+qP);
			
			int sPrio = (Integer) qP.getSingleResult();
			//System.out.println("Prio "+sPrio.toString());
			if (sPrio == 0)
			{
			 	Query q = em.createNamedQuery("get totaldata");
			 	List<DGraph> li = q.getResultList();
	
			 	//List li = (List) em.createQuery("Select u.first_name, d.phoneNumber, sum(d.dataUsage) from User as u, Data as d where d.phoneNumber = u.phone_number group by d.phoneNumber").getResultList();
				
			 	System.out.println("list users with total data: "+li);
				em.getTransaction().commit(); 
				
				//em.close();
				//emf.close();
	
				return li;
			}
			else
			{				
				Query q = em.createNamedQuery("get User_totaldata");
				q.setParameter("phno", phonenumber);
			 	List<DGraph> li = q.getResultList();
			
				em.getTransaction().commit(); 	
				return li;
			}
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}
	}
	/*Arpita change end*/
	
	//Changed by Aneri
	public Long GetDataByDate(String fromdate, String todate,
			String phone_number) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		try {
			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");			
			//Date startDate = null,endDate=null;
			/*try {
				startDate = dateFormat.parse(fromdate);
				endDate = dateFormat.parse(todate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			Query q = em
					.createQuery("Select sum(d.dataUsage) from Data d where d.currDate between :startDate and :endDate and "
							+ "d.phoneNumber=:phone_number ");

			System.out.println("query = " + q);
			//q.setParameter("startDate", startDate, TemporalType.DATE);
			//q.setParameter("endDate", endDate, TemporalType.DATE);
			q.setParameter("startDate", fromdate);
			q.setParameter("endDate", todate);
			q.setParameter("phone_number", phone_number);

			//System.out.println("startDate = " + startDate.toString());
			//System.out.println("endDate = " + endDate.toString());

			em.getTransaction().commit();
			return (Long) q.getSingleResult();
		} catch (NullPointerException ne) {
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}
	//Change ends
	
}
