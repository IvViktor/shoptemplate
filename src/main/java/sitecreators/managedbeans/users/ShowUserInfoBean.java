package sitecreators.managedbeans.users;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="showUserInfoBean")
public class ShowUserInfoBean {
	
	@ManagedProperty(value="#{param.ownerId}")
	private String ownerId;
	
	private User owner;
	
	private UserAbout about;
	
	private UserContacts contacts;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;
	
	private User user;
	
	private UserDAO userDao; 
	
	public ShowUserInfoBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		this.user = userDao.getUser(userId);
		this.owner = userDao.getUser(Long.parseLong(ownerId));
		this.about = owner.getAbout();
		this.contacts = owner.getContacts();
		this.icon = owner.getIcon();
		this.images = owner.getImages();
		this.comments = owner.getComments();
	}
	
	public void addComment(String body){
		Comment comment = new Comment();
		comment.setBody(body);
		comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		owner.addComment(comment);
		userDao.updateUser(owner);
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
