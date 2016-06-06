package DAL.Implementation;

//Change by Aneri

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.eclipselink.entity.User;
import DAL.UserDAO;

public class UserDAOImpl implements UserDAO {
	public User findByUserEmail(String email) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();

		User user = em.find(User.class, email);
		System.out.println( "user input"+user );
		em.close();
		emf.close();

		return user;
	}

	public String InsertUser(User user) {
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("jpa.eclipselink.entity");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			User user1 = new User(user.getFirst_name(), user.getLast_name(),
					user.getEmail(), user.getPassword(),
					user.getPhone_number(), user.getPriority(),
					user.getQuota(), user.getQuota_val(),user.getThreshold(), 
					user.getThreshold_val(),
					user.getData_flag(),
					user.getIs_delete(), user.getIs_valid(),user.getData_limit());
			System.out.println("insert user : "+user1);
			em.persist(user1);
			em.getTransaction().commit();

			em.close();
			emf.close();
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}
	
	public List<User> getuserDetails()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		try
		{
		 	Query q = em.createNamedQuery("Get Users");
		 	List<User> li = q.getResultList();
			System.out.println("list users : "+li);
			em.getTransaction().commit(); 
			//em.close();
			//emf.close();

			return li;		
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	public String UpdateUser(User user) {
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("jpa.eclipselink.entity");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			//System.out.println( "user input"+user );
			String email=user.getEmail();
			//System.out.println( "Email"+ email );
			User user1=em.find(User.class,email);
			//before update
			//System.out.println( "user1 before update"+user1 );
			user1.setFirst_name(user.getFirst_name());
			user1.setLast_name(user.getLast_name());
			user1.setQuota(user.getQuota());
			user1.setThreshold(user.getThreshold());
			user1.setData_flag(user.getData_flag());
			
			em.getTransaction().commit();
	        
			//after update
			System.out.println( "user1 after update"+user1 );
			
			em.close();
			emf.close();
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}
	
	public String DeleteUser(String email,String phone_number) {
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("jpa.eclipselink.entity");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			User user=em.find(User.class,email);
			em.remove(user);
			em.getTransaction().commit();
	        em.close();
			emf.close();
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	//Changed by Aneri
	public List<User> findLimitsByPhone(String phone_number) throws NullPointerException,NoResultException
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.eclipselink.entity");		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); 
		try
		{
		 	Query q = em.createNamedQuery("Get data limits");
		 	q.setParameter("phone_number", phone_number);
		 	List<User> r = q.getResultList();
			System.out.println("list users : "+ r);
			return r;	
		}
		catch(NullPointerException ne)
		{
			return null;
		}
		catch (NoResultException e) {
			return null;
		}
	}
	//Change ends

}
