package sitecreators.utils.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sitecreators.utils.SessionFactoryUtil;

public class UserDAOImpl implements UserDAO {

	private SessionFactory sessionFactory; 
	
	private Session session = null;

	public UserDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();
	}
	
	@Override
	public User getUser(long id) {
		Transaction tx = null;
		User user = null;
		try{
			tx = session.beginTransaction();
			user = session.get(User.class, id);
			tx.commit();
		} catch (Exception e){
			if (tx != null) tx.rollback();
		} 
		return user;
	}

	@Override
	public List<User> getUsers() {
		List<User> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		} 
		return resultList;
	}

	@Override
	public List<User> getUsers(String namePattern) {
		Transaction tx = null;
		List<User> users = new LinkedList<>();
		String[] tokens = namePattern.split("[;,.?! ]");
		try{
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.user.User U WHERE U.about.firstName LIKE :pattern OR "+
			"U.about.secondName LIKE :pattern";
			Query query = session.createQuery(hql);
			for(String pattern : tokens){
				query.setParameter("pattern", pattern);
				users.addAll((List<User>) query.list());
			}
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
		return users;
	}

	@Override
	public void addUser(User user) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void updateUser(User user) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void removeUser(User user) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void open() {
		if(session == null) this.session = this.sessionFactory.openSession();
		System.out.println("userDAO session opened");
	}

	@Override
	public void close() {
		if(session != null){
			this.session.close();
			this.session = null;
			System.out.println("categoryDAO session opened");
		}
	}

	@Override
	public User getUser(String email) {
		Transaction tx = null;
		List<User> users = new LinkedList<>();
		try{
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.user.User U WHERE U.contacts.email = :login";
			Query query = session.createQuery(hql);
			query.setParameter("login", email);
			users.addAll((List<User>) query.list());
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} 
		if(users.size() > 0) return users.get(0);
		return null;
	}

}
