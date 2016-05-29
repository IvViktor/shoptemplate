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
	
	private Session session=null;
	
	public CategoryDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();	
	}
	
	public void open(){
		if(this.session == null) this.session = this.sessionFactory.openSession();
		System.out.println("categoryDAO session opened");
	}
	
	public void close(){
		if(session != null){
			this.session.close();
			this.session=null;
			System.out.println("categoryDAO session closed");
		}
	}
	
	@Override
	public Category getCategory(String title) {
		List<Category> resultList = null;
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.category.Category Cat WHERE Cat.title = :cat_title";
			Query query = session.createQuery(hql);
			query.setParameter("cat_title",title);
			resultList = (List<Category>) query.list();
			tx.commit();
		} catch(Exception e){
			if(tx != null) tx.rollback();
			return null;
		} 
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override
	public void addCategory(Category category) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void removeCategory(Category category) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Category.class);
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		} 
		return resultList;
	}

	@Override
	public void updateCategory(Category category) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(category);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

}
