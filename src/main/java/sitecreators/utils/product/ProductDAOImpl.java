package sitecreators.utils.product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import sitecreators.utils.SessionFactoryUtil;

public class ProductDAOImpl implements ProductDAO {

	private SessionFactory sessionFactory; 
	
	public ProductDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}
	
	@Override
	public Product getProduct(long id) {
		Session session = null;
		Transaction tx = null;
		Product product = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			product = session.get(Product.class, id);
			tx.commit();
		} catch (Exception e){
			if (tx != null) tx.rollback();
		} finally {
			session.close();
		}
		return product;
	}

	@Override
	public List<Product> getProducts() {
		List<Product> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
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
	public List<Product> getProducts(String titleRegExp) {
		List<Product> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			cr.add(Restrictions.like("productTitle", titleRegExp));
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
	public void addProduct(Product product) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void updateProduct(Product product) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void removeProduct(Product product) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

}
