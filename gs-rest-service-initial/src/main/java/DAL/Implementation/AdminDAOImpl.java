package DAL.Implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.eclipselink.entity.User;
import DAL.AdminDAO;

public class AdminDAOImpl implements AdminDAO {
	public String adminsignup(String first_name, String last_name, String email, String password, String phone_number)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		User user = new User();
		
		user.setEmail(email);
		user.setFirst_name(first_name);
		user.setLast_name(last_name);
		user.setPassword(password);
		user.setPhone_number(phone_number);
		
		em.persist(user);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return "true";
	}
	
	public String findAdminByEmail(String email) throws NullPointerException, NoResultException
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		try
		{
		 	Query q = em.createNamedQuery("find user by email");
			q.setParameter("email", email);				
		
			//User user = (User) q.getSingleResult();
			//System.out.println("query result = "+user);
			
			em.getTransaction().commit(); 
			//em.close();
			//emf.close();
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
	public String findAdminByPhone(String phone_number) throws NullPointerException,NoResultException
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		try
		{
		 	Query q = em.createNamedQuery("find user by phone_number");
			q.setParameter("phone_number", phone_number);				
		
			//User user = (User) q.getSingleResult();
			//System.out.println("query result = "+user);
			
			em.getTransaction().commit(); 
			//em.close();
			//emf.close();
			System.out.println("after query user = "+q.getSingleResult());
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
}
