package sitecreators.utils.finance;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sitecreators.utils.SessionFactoryUtil;

public class CurrencyDAOImpl implements CurrencyDAO {
	
	private SessionFactory sessionFactory = null;
	
	public CurrencyDAOImpl() {
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}

	@Override
	public List<Currency> getAllCurrencies() {
		List<Currency> resultList = new ArrayList<>();
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Currency.class);
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
	public void removeCurrency(Currency currency) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(currency);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void updateCurrencies(List<Currency> updateList) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			for(Currency currency : updateList) session.update(currency);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void addCurrency(Currency currency) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(currency);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void updateCurrency(Currency currency) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(currency);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}
}
