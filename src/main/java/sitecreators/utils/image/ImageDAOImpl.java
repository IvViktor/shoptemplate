package sitecreators.utils.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.Part;

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

	@Override
	public String saveImage(Part part, String directory, long userId) {
		String webappRoot = File.separator + "opt" +File.separator + "tomcat" +File.separator + "webapps";
		String defaultIcon = File.separator + "shopImageData"+File.separator + directory + File.separator + "noimage.gif";
		String fileName = getFileName(part);
		if(fileName!=null){
			String filePath = File.separator + "shopImageData" + File.separator + directory + File.separator + userId;
			try (InputStream input = part.getInputStream()) {
				File path = new File(webappRoot+filePath);
				path.mkdirs();
				File image = new File(path,fileName);
		        Files.copy(input, image.toPath());
		    }
		    catch (IOException e) {
		        return defaultIcon;
		    }
			return filePath + File.separator + fileName;
		}
		return defaultIcon;
	}
	
	private String getFileName(Part part) {
	    try{
		for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	    } catch (Exception e){
	    	return null;
	    }
	}

}
