package sitecreators.managedbeans.bootshop;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="userEditorBean")
public class UserEditorBean {
	
	private Image icon;
	
	private List<Image> images;
	
	private User user;
	
	private long userId;
	
	private UserDAO userDao; 
	
	private ImageDAO imageDao;
	
	private String firstName;
	
	private String secondName;
	
	private String userName;

	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	private List<Order> cart = new ArrayList<>();

	private int totalPrice;
	
	private String userAbout;
	
	private String pnoneNumber;

	private String email;

	private String oldPassword;
	
	private String newPassword;
	
	private Image newIcon;
	
	private Part newImage;
	
	private String newImageDesc;
	
	private Password password;
	
	private List<Product> products;
	
	private int pageNum = 1;
	
	public UserEditorBean() throws Exception{
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try{
			String page = (String) req.getParameter("page");
			this.pageNum = Integer.parseInt(page);
		} catch (Exception e){
			pageNum = 1;
		}
		if(pageNum == 0) pageNum++;
		try{
			userDao.open();
			this.user = userDao.getUser(userId);
			UserAbout uAbout = user.getAbout();
			if(uAbout != null){
				this.firstName = uAbout.getFirstName();
				this.secondName = uAbout.getSecondName();
				this.userName = firstName+" "+secondName;
				this.userAbout = uAbout.getAboutInfo();
			}
			UserContacts uContact = user.getContacts();
			if(uContact != null){
				this.pnoneNumber = uContact.getPnoneNumber();
				this.email = uContact.getEmail();
			}
			List<Order> orders = user.getPurchases();
			for(Order o : orders){
				if(o.getStatus().equals(OrderStatus.INCART)) this.cart.add(o);
			}
			this.icon = user.getIcon();
			this.images = user.getImages();
			this.password = user.getPassword();
			List<Product> selling = user.getSelling();
			Collections.reverse(selling);
			int size = 6;
			int endIndex = size * pageNum;
			if(selling.size() < endIndex) endIndex = selling.size();
			this.products = selling.subList((pageNum-1)*size, endIndex);
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception("Unknown user.");
		}
		try{
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
		} catch (Exception e){
			e.printStackTrace();
		}
		calculateSum();
	}
	
	private void calculateSum(){
		this.totalPrice = 0;
		for(Order order : cart){
			int number = order.getProductsNumber();
			int price = order.getProduct().getProductPrice().getAmount();
			this.totalPrice += (price * number);
		}
	}
	
	public void changePassword(){
		try {
			if(password.check(oldPassword)){
				password.create(newPassword);
				userDao.open();
				userDao.updateUser(user);
				userDao.close();
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
	
	public void closeSession(){
		userDao.close();
	}
	
	public String save(){
		UserAbout uAbout = user.getAbout();
		if(uAbout == null) uAbout = new UserAbout();
		uAbout.setFirstName(firstName);
		uAbout.setSecondName(secondName);
		uAbout.setAboutInfo(userAbout);
		userName = firstName+" "+secondName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Order> getCart() {
		return cart;
	}

	public void setCart(List<Order> cart) {
		this.cart = cart;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
		
}
