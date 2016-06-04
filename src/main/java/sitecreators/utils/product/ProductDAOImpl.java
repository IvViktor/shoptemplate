package sitecreators.utils.product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import sitecreators.utils.SessionFactoryUtil;
import sitecreators.utils.category.Category;

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
	public List<Product> getProducts(int startNum, int length) {
		List<Product> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			cr.setFirstResult(startNum);
			cr.setMaxResults(length);
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
			e.printStackTrace();
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
		System.out.println("productDAO session opened");
	}

	@Override
	public void close() {
		if(this.session != null){
			this.session.close();
			this.session = null;
			System.out.println("productDAO session closed");
		}
	}

	@Override
	public List<Product> getProducts(Category category, double minPrice, double maxPrice) {
		List<Product> resultList = new ArrayList<>();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			if(category != null) cr.add(Restrictions.eq("category", category));
			if(maxPrice != 0)  cr.add(Restrictions.between("productPrice.amount", minPrice, maxPrice));
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
		}
		return resultList;
	}

}
