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
	
	public UserDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}
	
	@Override
	public User getUser(long id) {
		Session session = null;
		Transaction tx = null;
		User user = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			user = session.get(User.class, id);
			tx.commit();
		} catch (Exception e){
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public List<User> getUsers() {
		List<User> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		} finally {
			session.close();
		}
		return resultList;
	}

	@Override
	public List<User> getUsers(String namePattern) {
		Session session = null;
		Transaction tx = null;
		List<User> users = new LinkedList<>();
		String[] tokens = namePattern.split("[;,.?! ]");
		try{
			session = this.sessionFactory.openSession();
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
		} finally {
			session.close();
		}
		return users;
	}

	@Override
	public void addUser(User user) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void updateUser(User user) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void removeUser(User user) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

}
