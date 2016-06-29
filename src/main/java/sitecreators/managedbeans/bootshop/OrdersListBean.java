package sitecreators.managedbeans.bootshop;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math3.util.Precision;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.finance.Country;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="ordersBean")
public class OrdersListBean {
	
	private User user;

	private UserDAO userDao; 
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	private long productId;

	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private String totalPrice;
	
	private String totalDiscount;
	
	private Currency userCurrency;
	
	private List<Order> orders;
	
	private OrderStatus[] statusList;
	
	public OrdersListBean() throws Exception{
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try{
			String id = (String) req.getParameter("userId");
			userId = Long.parseLong(id);
		} catch (Exception e){
			try{
				userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
			} catch (NullPointerException eIn){
				userId = 0;
				throw new Exception("Unauthorised access");
			}
		}
		try{
			String id = (String) req.getParameter("productId");
			productId = Long.parseLong(id);
		} catch (Exception e){
			productId = 0;
		}
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
			calculateSum();
			if(productId == 0){
				this.orders = user.getPurchases();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
		} catch (Exception e){
			e.printStackTrace();
		}
		if(productId > 0){
			try{
				
				productDao.open();
				Product product = productDao.getProduct(productId);
				if(userId != product.getOwner().getId()) throw new IllegalArgumentException();
				this.orders = product.getOrders();
			} catch (IllegalArgumentException e){
				throw new Exception("Cannot provide access for non owner");
			} catch (Exception e){
				
			}
		}
		statusList = OrderStatus.values();
	}
	
	private void calculateSum(){
		double price = 0;
		double discount = 0;
		for(Order order : cart){
			int number = order.getProductsNumber();
			ProductPrice pPrice = order.getProduct().getProductPrice();
			Currency curr = pPrice.getCurrency();
			double amount = pPrice.getAmount();
			amount = (amount * number) * userCurrency.getKoef() / curr.getKoef();
			double disc = amount * pPrice.getDiscount() / 100;
			discount+=disc;
			price+=(amount - disc);
		}
		
		char cc = userCurrency.getCountryCode().getCc();
		if(userCurrency.getCountryCode().isPositionLeft()){
			this.totalPrice = String.valueOf(cc) + Precision.round(price, 2);
			this.totalDiscount = String.valueOf(cc) + Precision.round(discount, 2);
		} else {
			this.totalPrice = Precision.round(price, 2) + String.valueOf(cc);
			this.totalDiscount = Precision.round(discount, 2) + String.valueOf(cc);
		}
	}
	
	public String returnPrice(ProductPrice pPrice, int number){
		Currency productCurrency = pPrice.getCurrency();
		StringBuffer price = new StringBuffer();
		Country code = userCurrency.getCountryCode();
		if(code.isPositionLeft()) price.append(code.getCc());
		double amount = pPrice.getAmount() * number * userCurrency.getKoef() / productCurrency.getKoef();
		double discount = pPrice.getAmount() * number * pPrice.getDiscount() / 100 * userCurrency.getKoef() / productCurrency.getKoef();
		amount -= discount;
		price.append(Precision.round(amount, 2));
		if(!code.isPositionLeft()) price.append(code.getCc());
		return price.toString();
	}
	
	public String returnDiscount(ProductPrice pPrice, int number){
		Currency productCurrency = pPrice.getCurrency();
		StringBuffer price = new StringBuffer();
		Country code = userCurrency.getCountryCode();
		if(code.isPositionLeft()) price.append(code.getCc());
		double discount = pPrice.getAmount() * number * pPrice.getDiscount() / 100 * userCurrency.getKoef() / productCurrency.getKoef();
		price.append(Precision.round(discount, 2));
		if(!code.isPositionLeft()) price.append(code.getCc());
		return price.toString();
	}
	
	public void changeStatus(String orderId, String newStatus){
		long id = 0;
		try{
			id = Long.parseLong(orderId);
		} catch (Exception e){
			return;
		}
		Order order = null;
		for(Order o : this.orders){
			if(o.getId() == id) order = o;
		}
		if(order != null){
			OrderStatus ps = OrderStatus.valueOf(newStatus);
			order.setStatus(ps);
		}
	}
	
	public void closeSession(){
		userDao.close();
		productDao.close();
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getTotalPrice() {
		return totalPrice;
	}
	
	public String getTotalDiscount() {
		return totalDiscount;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTotalDiscount(String totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public OrderStatus[] getStatusList() {
		return statusList;
	}

	public void setStatusList(OrderStatus[] statusList) {
		this.statusList = statusList;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
			
}
