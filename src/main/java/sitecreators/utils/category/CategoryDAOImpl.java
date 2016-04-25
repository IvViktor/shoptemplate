package sitecreators.utils.category;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sitecreators.utils.SessionFactoryUtil;

public class CategoryDAOImpl implements CategoryDAO {
	
	private SessionFactory sessionFactory; 
	
	public CategoryDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}
	
	@Override
	public Category getCategory(String title) {
		List<Category> resultList = null;
		Transaction tx = null;
		Session session = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.category.Category Cat WHERE Cat.title = :cat_title";
			Query query = session.createQuery(hql);
			query.setParameter("cat_title",title);
			resultList = (List<Category>) query.list();
			tx.commit();
		} catch(Exception e){
			if(tx != null) tx.rollback();
			return null;
		} finally {
			session.close();
		}
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override
	public void addCategory(Category category) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void removeCategory(Category category) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Category.class);
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
	public void updateCategory(Category category) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

}
