package sitecreators.managedbeans.bootshop;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="productDetailsBean")
public class ProductDetailsBean {
	
	private User user;

	private UserDAO userDao; 

	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Product> products;
	
	private List<Category> categories;
	
	private String loginEmail;
	
	private String loginPassword;
	
	private Product product;
	
	private String productId;
	
	private String title;
	
	private String description;
	
	private int price;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;
	
	private String commentBody;
	
	public ProductDetailsBean() throws Exception{
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		try{
			userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (NullPointerException e){
			userId = 0;
		}
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.productId = (String) req.getParameter("productId");
		try{
			userDao.open();
			this.user = userDao.getUser(userId);
			UserAbout uAbout = user.getAbout();
			if(uAbout != null){
				this.userName = uAbout.getFirstName()+" "+uAbout.getSecondName();
			}
			List<Order> orders = user.getPurchases();
			for(Order o : orders){
				if(o.getStatus().equals(OrderStatus.INCART)) this.cart.add(o);
			}
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
		try{
			long id = Long.parseLong(productId);
			productDao.open();
			this.product = productDao.getProduct(id);
			if(product == null) throw new Exception();
			this.products = productDao.getProducts(product.getCategory(),0,0,0,6);
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("invalid identifier");
		}
		
	}
	
	public void closeSession(){
		productDao.close();
		userDao.close();
		categoryDao.close();
	}
	
	public void login(){
		try{
			userDao.open();
			this.user = userDao.getUser(loginEmail);
			Password pswd = user.getPassword();
			if(pswd.check(loginPassword)){
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userID", user.getId());
				System.out.println("password accepted");
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
	}
	
	public void addToCart(long productId){
		Order order = new Order();
		order.setCustomer(user);
		order.setFormedTime(new Timestamp(new Date().getTime()));
		order.setStatus(OrderStatus.INCART);
		try{
			productDao.open();
			Product product = productDao.getProduct(productId);
			product.addOrder(order);
			productDao.updateProduct(product);
			userDao.open();
			user.addPurchase(order);
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			productDao.close();
			userDao.close();
		}		
	}
	
	public void addToCart(){
		Order order = new Order();
		order.setCustomer(user);
		order.setFormedTime(new Timestamp(new Date().getTime()));
		order.setStatus(OrderStatus.INCART);
		try{
			productDao.open();
			product.addOrder(order);
			productDao.updateProduct(product);
			userDao.open();
			user.addPurchase(order);
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			productDao.close();
			userDao.close();
		}		
	}
	
	public void addComment(){
		Comment comment = new Comment();
		comment.setBody(commentBody);
		comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		this.product.addComment(comment);
		try{
			productDao.open();
			productDao.updateProduct(product);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			productDao.close();
		}
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

	public List<Order> getCart() {
		return cart;
	}

	public void setCart(List<Order> cart) {
		this.cart = cart;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public String getCommentBody() {
		return commentBody;
	}

	public void setCommentBody(String commentBody) {
		this.commentBody = commentBody;
	}
		
}
