package sitecreators.utils.image;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sitecreators.utils.SessionFactoryUtil;

public class ImageDAOImpl implements ImageDAO {
	
private SessionFactory sessionFactory; 
	
	public ImageDAOImpl(){
		this.sessionFactory = SessionFactoryUtil.getSessionFactory();		
	}
	
	@Override
	public Image getImage(long id) {
		List<Image> resultList = null;
		Transaction tx = null;
		Session session = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.image.Image IMG WHERE IMG.id = :img_id";
			Query query = session.createQuery(hql);
			query.setParameter("img_id",id);
			resultList = query.list();
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
	public Image getImage(String path) {
		List<Image> resultList = null;
		Transaction tx = null;
		Session session = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			String hql = "FROM sitecreators.utils.image.Image IMG WHERE IMG.imagePath = :img_path";
			Query query = session.createQuery(hql);
			query.setParameter("img_path",path);
			resultList = query.list();
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
	public void addImage(Image image) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(image);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void removeImage(Image image) {
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(image);
			tx.commit();
		} catch (Exception e){
			if(tx != null) tx.rollback();
		} finally {
			session.close();
		}
	}

}
