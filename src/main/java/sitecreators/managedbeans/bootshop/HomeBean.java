package sitecreators.managedbeans.bootshop;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.image.Image;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserContacts;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="homeBean")
public class HomeBean {
	
	private User user;
	
	private User visitor;

	private UserDAO userDao; 

	private long visitorId = 0;
	
	private long userId;
	
	private String userName;
	
	private String visitorName;
	
	private List<Order> cart = new ArrayList<>();
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
		
	private String userPhone;
	
	private String userEmail;
	
	private String userAbout;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Order> purchases;
	
	private int totalPrice;
	
	public HomeBean() throws Exception{
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		try{
			visitorId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (NullPointerException e){
			visitorId = 0;
		}
		if(visitorId == 0) throw new Exception("unauthorised user.");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try{
			userId = Long.parseLong((String) req.getParameter("userId"));
		} catch (NumberFormatException | NullPointerException e){
			userId = visitorId;
		}
		try{
			userDao.open();
			// Visitor info///////////////////////////////
			this.visitor = userDao.getUser(visitorId);
			UserAbout vAbout = visitor.getAbout();
			if(vAbout != null){
				this.visitorName = vAbout.getFirstName()+" "+vAbout.getSecondName();
			}
			List<Order> orders = visitor.getPurchases();
			for(Order o : orders){
				if(o.getStatus().equals(OrderStatus.INCART)) this.cart.add(o);
			}
			
			// User info//////////////////////////////////
			this.user = userDao.getUser(userId);
			UserAbout uAbout = user.getAbout();
			if(uAbout != null){
				this.userName = uAbout.getFirstName()+" "+uAbout.getSecondName();
				this.userAbout = uAbout.getAboutInfo();
			}
			UserContacts uContact = user.getContacts();
			if(uContact != null){
				this.userPhone = uContact.getPnoneNumber();
				this.userEmail = uContact.getEmail();
			}
			this.icon = user.getIcon();
			this.images = user.getImages();
			orders = user.getPurchases();
			Collections.reverse(orders);
			int endIndex = 6;
			if(orders.size() < endIndex) endIndex = orders.size();
			this.purchases = orders.subList(0, endIndex);
		} catch (Exception e){
			e.printStackTrace();
		} 		
		try{
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			categoryDao.close();
		}
		calculateSum();
	}
	
	public void closeSession(){
		productDao.close();
		userDao.close();
		categoryDao.close();
	}
	
		
	public void addToCart(long productId){
		try{
			productDao.open();
			Product product = productDao.getProduct(productId);
			Order order = checkOrder(product);
			if(order == null){
				order = new Order();
				order.setCustomer(user);
				order.setFormedTime(new Timestamp(new Date().getTime()));
				order.setStatus(OrderStatus.INCART);
				product.addOrder(order);
				cart.add(order);
			} else {
				int amount = order.getProductsNumber();
				amount++;
				order.setProductsNumber(amount);
			}
			productDao.updateProduct(product);
			userDao.open();
			visitor.addPurchase(order);
			userDao.updateUser(visitor);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			productDao.close();
			userDao.close();
		}
		calculateSum();
	}
	
	private Order checkOrder(Product product){
		for(Order order : cart){
			if(order.getProduct().getId() == product.getId()) return order;
		}
		return null;
	}
	
	private void calculateSum(){
		this.totalPrice = 0;
		for(Order order : cart){
			int number = order.getProductsNumber();
			int price = order.getProduct().getProductPrice().getAmount();
			this.totalPrice += (price * number);
		}
	}

	public User getVisitor() {
		return visitor;
	}

	public void setVisitor(User visitor) {
		this.visitor = visitor;
	}

	public long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(long visitorId) {
		this.visitorId = visitorId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public List<Order> getCart() {
		return cart;
	}

	public void setCart(List<Order> cart) {
		this.cart = cart;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
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

	public List<Order> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Order> purchases) {
		this.purchases = purchases;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserAbout() {
		return userAbout;
	}

	public void setUserAbout(String userAbout) {
		this.userAbout = userAbout;
	}

	}
