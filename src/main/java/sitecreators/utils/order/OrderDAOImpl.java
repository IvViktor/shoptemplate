package sitecreators.utils.order;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sitecreators.utils.SessionFactoryUtil;

public class OrderDAOImpl implements OrderDAO {

	private SessionFactory sessionFactory; 
	
	public OrderDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}
	
	@Override
	public List<Order> getOrders() {
		List<Order> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Order.class);
			resultList = cr.list();
			tx.commit();
		} catch (Exception e){
			if(tx !=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return resultList;
	}

}
