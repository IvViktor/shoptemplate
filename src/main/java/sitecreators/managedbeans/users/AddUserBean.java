package sitecreators.managedbeans.users;

import java.security.NoSuchAlgorithmException;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="addUserBean")
public class AddUserBean {
	
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
			return "internalError";
		}
		user.setPassword(pass);
		ImageDAO imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		Image icon = new Image();
		String filePath = imageDao.saveImage(null, "users", 0);
		icon.setImagePath(filePath);
		icon.setImgDecs("no image");
		imageDao.addImage(icon);
		user.setIcon(icon);
		try{
			UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
			userDao.open();
			userDao.addUser(user);
			userDao.close();
		} catch (Exception e){
			e.printStackTrace();
			return "internalError";
		}
		
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
