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
	
	private Session session = null;
	
	public ProductDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();	
	}
	
	@Override
	public Product getProduct(long id) {
		Transaction tx = null;
		Product product = null;
		try{
			tx = session.beginTransaction();
			product = session.get(Product.class, id);
			tx.commit();
		} catch (Exception e){
			if (tx != null) tx.rollback();
		} 
		return product;
	}

	@Override
	public List<Product> getProducts() {
		List<Product> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		} 
		return resultList;
	}

	@Override
	public List<Product> getProducts(String titleRegExp) {
		List<Product> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			cr.add(Restrictions.like("productTitle", titleRegExp));
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		}
		return resultList;
	}

	@Override
	public void addProduct(Product product) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void updateProduct(Product product) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.update(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void removeProduct(Product product) {
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(product);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} 
	}

	@Override
	public void open() {
		if(session == null) this.session = this.sessionFactory.openSession();
	}

	@Override
	public void close() {
		this.session.close();
		this.session = null;
	}

}
