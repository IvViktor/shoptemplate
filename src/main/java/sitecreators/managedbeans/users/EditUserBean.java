package sitecreators.managedbeans.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="editUserBean")
public class EditUserBean {
	
	private Image icon;
	
	private List<Image> images;
	
	private User user;
	
	private UserDAO userDao; 
	
	private ImageDAO imageDao;
	
	private String firstName;
	
	private String secondName;
	
	private String userAbout;
	
	private String pnoneNumber;

	private String email;

	private String oldPassword;
	
	private String newPassword;
	
	private List<Comment> comments;
	
	private Comment selectedComment;
	
	private Image newIcon;
	
	private Part newImage;
	
	private String newImageDesc;
	
	private Password password;
	
	public EditUserBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		long userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		try{
			userDao.open();
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
			this.password = user.getPassword();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void changePassword(){
		try {
			if(password.check(oldPassword)){
				password.create(newPassword);
			}
			else System.err.println("invalid password");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Could not create new password");
			e.printStackTrace();
		}
	}
	
	public void addImage(){
		Image newImage = new Image();
		String filePath = imageDao.saveImage(this.newImage, "users", this.user.getId());
		newImage.setImagePath(filePath);
		newImage.setImgDecs(newImageDesc);
		imageDao.addImage(newImage);
		user.addImage(newImage);
	}
	
	public void setNewIcon(){
		user.setIcon(newIcon);
	}
	
	public void removeComment(){
		user.removeComment(selectedComment);
	}
	
	public void closeSession(){
		userDao.close();
	}
	
	public String save(){
		UserAbout uAbout = user.getAbout();
		if(uAbout == null) uAbout = new UserAbout();
		uAbout.setFirstName(firstName);
		uAbout.setSecondName(secondName);
		uAbout.setAboutInfo(userAbout);
		UserContacts uContact = user.getContacts();
		if(uContact == null) uContact = new UserContacts();
		uContact.setEmail(email);
		uContact.setPnoneNumber(pnoneNumber);
		try{
			userDao.open();
			userDao.updateUser(user);
			userDao.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return "home";
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

	public String getUserAbout() {
		return userAbout;
	}

	public void setUserAbout(String userAbout) {
		this.userAbout = userAbout;
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment getSelectedComment() {
		return selectedComment;
	}

	public void setSelectedComment(Comment selectedComment) {
		this.selectedComment = selectedComment;
	}

	public Part getNewImage() {
		return newImage;
	}

	public void setNewImage(Part newImage) {
		this.newImage = newImage;
	}

	public String getNewImageDesc() {
		return newImageDesc;
	}

	public void setNewImageDesc(String newImageDesc) {
		this.newImageDesc = newImageDesc;
	}

	public Image getNewIcon() {
		return newIcon;
	}

	public void setNewIcon(Image newIcon) {
		this.newIcon = newIcon;
	}
		
}
