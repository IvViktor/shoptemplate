package sitecreators.managedbeans.users;

import java.security.NoSuchAlgorithmException;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="addUserBean")
public class AddUser {
	
	private String firstName;
	
	private String secondName;
	
	private String email;
	
	private String password;
	
	public String addNewUser(){
		User user = new User();
		UserAbout about = new UserAbout();
		about.setFirstName(firstName);
		about.setSecondName(secondName);
		user.setAbout(about);
		UserContacts contacts = new UserContacts();
		contacts.setEmail(email);
		user.setContacts(contacts);
		Password pass = new Password();
		try {
			pass.create(this.password);
		} catch (NoSuchAlgorithmException e) {
			return "intererrorpage";
		}
		user.setPassword(pass);	 
		UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		userDao.addUser(user);
		
		return "home";
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
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
		this.password = password.trim();
	}

}
