package sitecreators.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {
	
	private static SessionFactory instance;
	
	public static SessionFactory getSessionFactory(){
		if(instance!=null){
			return instance;
		} else{
			instance = new Configuration().configure().buildSessionFactory();
			return instance;
		}
	}
}
