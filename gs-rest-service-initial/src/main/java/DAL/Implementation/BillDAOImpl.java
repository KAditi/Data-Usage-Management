package DAL.Implementation;


import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpa.eclipselink.entity.Billingcycle;
import DAL.BillDAO;

public class BillDAOImpl implements BillDAO {

	public String selectcycle(String fromday)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		
		try
		{
		 	Query q = em.createNamedQuery("Get Cycle");
			System.out.println("query q "+q);							
			em.getTransaction().commit(); 
			
			return (String)q.getSingleResult();		
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}
	}

	public String updatecycle(String fromday) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		Query q = em.createNamedQuery("Update Cycle");
		q.setParameter("fromday", fromday);
		
		Integer qint = q.executeUpdate();
		
		em.getTransaction().commit();
		if (qint>0)
			return "true";
		else
			return "false";
	}


	public String insertcycle(String fromday) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		Billingcycle b = new Billingcycle();		
		b.setFromday(fromday);
				
		em.persist(b);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return "true";
	}

}
