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
import sitecreators.utils.finance.Country;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.image.Image;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductDecription;
import sitecreators.utils.product.ProductPrice;
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
	
	private String price;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;
	
	private String commentBody;

	private String totalPrice;

	private Currency userCurrency;
	
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
			this.userCurrency = user.getCurrency();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
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
			this.title = product.getProductTitle();
			ProductDecription pDescr = product.getDescription();
			if(pDescr != null) this.description = pDescr.getDescription();
			this.price = returnPrice(product.getProductPrice());
			this.icon = product.getIcon();
			this.images = product.getImages();
			this.comments = product.getComments();
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("invalid identifier");
		}
		calculateSum();
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
				this.userId = user.getId();
				this.user = userDao.getUser(userId);
				UserAbout uAbout = user.getAbout();
				if(uAbout != null){
					this.userName = uAbout.getFirstName()+" "+uAbout.getSecondName();
				}
				List<Order> orders = user.getPurchases();
				for(Order o : orders){
					if(o.getStatus().equals(OrderStatus.INCART)) this.cart.add(o);
				}
				calculateSum();
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
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
			user.addPurchase(order);
			userDao.updateUser(user);
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
	
	public void addToCart(){
		try{
			productDao.open();
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
			user.addPurchase(order);
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			productDao.close();
			userDao.close();
		}
		calculateSum();
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
	
	private void calculateSum(){
		double price = 0;
		for(Order order : cart){
			int number = order.getProductsNumber();
			ProductPrice pPrice = order.getProduct().getProductPrice();
			Currency curr = pPrice.getCurrency();
			double amount = pPrice.getAmount();
			amount = (amount * number) * userCurrency.getKoef() / curr.getKoef();
			double disc = amount * pPrice.getDiscount() / 100;
			price+=(amount - disc);
		}
		char cc = userCurrency.getCountryCode().getCc();
		if(userCurrency.getCountryCode().isPositionLeft()){
			this.totalPrice = String.valueOf(cc) + price;
		} else {
			this.totalPrice = price + String.valueOf(cc);
		}
	}
	
	public String returnPrice(ProductPrice pPrice){
		Currency productCurrency = pPrice.getCurrency();
		StringBuffer price = new StringBuffer();
		Country code = userCurrency.getCountryCode();
		if(code.isPositionLeft()) price.append(code.getCc());
		double amount = pPrice.getAmount() * userCurrency.getKoef() / productCurrency.getKoef();
		price.append(amount);
		if(!code.isPositionLeft()) price.append(code.getCc());
		return price.toString();
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
		
}
