package sitecreators.managedbeans.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="editUserBean")
public class EditUserBean {
	
	private UserAbout about;
	
	private UserContacts contacts;
	
	private Image icon;
	
	private List<Image> images;
	
	private User user;
	
	private UserDAO userDao; 
	
	private ImageDAO imageDao;
	
	public EditUserBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		this.user = userDao.getUser(userId);
		this.about = user.getAbout();
		this.contacts = user.getContacts();
		this.icon = user.getIcon();
		this.images = user.getImages();
	}
	
	public String changePassword(String oldPassword,String newPassword){
		Password password = this.user.getPassword();
		try {
			if(password.check(oldPassword)){
				password.create(newPassword);
				return "passwordAccepted";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "internalError";
		}
		return "passwordIncorrect";
	}
	
	public void addImage(Part imageFile, String imageDesc){
		Image newImage = new Image();
		String filePath = imageDao.saveImage(imageFile, "users", this.user.getId());
		newImage.setImagePath(filePath);
		newImage.setImgDecs(imageDesc);
		imageDao.addImage(newImage);
		images.add(newImage);
	}
	
	public void setNewIcon(Image newIcon){
		images.add(icon);
		icon = newIcon;
		user.setIcon(newIcon);
		images.remove(newIcon);
	}
	
	public void save(){
		userDao.updateUser(user);
	}
	
	public UserAbout getAbout() {
		return about;
	}

	public void setAbout(UserAbout about) {
		this.about = about;
	}

	public UserContacts getContacts() {
		return contacts;
	}

	public void setContacts(UserContacts contacts) {
		this.contacts = contacts;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

}
