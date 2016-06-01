package sitecreators.managedbeans.users;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="showUserInfoBean")
public class ShowUserInfoBean {
	
	private User visitor;
	
	private String id;
	
	private ImageDAO imageDao;
	
	private String firstName;
	
	private String secondName;
	
	private String userAbout;
	
	private String pnoneNumber;

	private String email;

	private String newComment;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;
	
	private User user;
	
	private UserDAO userDao; 
	
	public ShowUserInfoBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		long visitorId = 1;//(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.id = (String) req.getParameter("userId");
		long userId = Long.parseLong(this.id);
		try{
			userDao.open();
			this.visitor = userDao.getUser(visitorId);
			this.user = userDao.getUser(userId);
			UserAbout uAbout = user.getAbout();
			if(uAbout != null){
				this.firstName = uAbout.getFirstName();
				this.secondName = uAbout.getSecondName();
				this.userAbout = uAbout.getAboutInfo();
			}
			UserContacts uContact = user.getContacts();
			if(uContact != null){
				this.pnoneNumber = uContact.getPnoneNumber();
				this.email = uContact.getEmail();
			}
			this.icon = user.getIcon();
			this.images = user.getImages();
			this.comments = user.getComments();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void closeSession(){
		userDao.close();
	}
	public void addComment(){
		Comment comment = new Comment();
		comment.setBody(newComment);
		comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		try{
			userDao.open();
			user.addComment(comment);
			userDao.updateUser(user);
			userDao.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPnoneNumber() {
		return pnoneNumber;
	}

	public void setPnoneNumber(String pnoneNumber) {
		this.pnoneNumber = pnoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
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

	public String getUserAbout() {
		return userAbout;
	}

	public void setUserAbout(String userAbout) {
		this.userAbout = userAbout;
	}
	
}
