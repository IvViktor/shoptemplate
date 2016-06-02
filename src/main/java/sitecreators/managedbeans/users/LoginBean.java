package sitecreators.managedbeans.users;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="loginBean")
public class LoginBean {
	
	private String email;
	
	private String password;
	
	private String nextPage;
	
	private UserDAO userDao;
	
	public LoginBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.nextPage = (String) req.getParameter("nextpage");
	}
	
	public String login(){
		try{
			userDao.open();
			User user = userDao.getUser(email);
			Password pswd = user.getPassword();
			if(pswd.check(password)){
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", user.getId());
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		if(nextPage != null) return nextPage;
		return "products";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
