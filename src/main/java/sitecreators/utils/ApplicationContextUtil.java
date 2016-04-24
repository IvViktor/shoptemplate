package sitecreators.utils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextUtil {
	private static final ApplicationContext ac = new ClassPathXmlApplicationContext("app_context.xml");
	
	public static ApplicationContext getApplicationContext(){
		return ac;
	}
	
}
