package sitecreators.managedbeans.bootshop;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.finance.Country;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="cartBean")
public class CartBean {
	
	private User user;

	private UserDAO userDao; 

	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	private String loginEmail;
	
	private String loginPassword;
	
	private String totalPrice;
	
	private String totalDiscount;
	
	private Currency userCurrency;
	
	public CartBean(){
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		try{
			userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (NullPointerException e){
			userId = 0;
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
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		try{
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
		} catch (Exception e){
			e.printStackTrace();
		}
		calculateSum();
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
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
	}
	
	public void incOrder(Order order){
		int amount = order.getProductsNumber();
		amount++;
		order.setProductsNumber(amount);
		try{
			userDao.open();
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		calculateSum();
	}
	
	public void decOrder(Order order){
		int amount = order.getProductsNumber();
		amount--;
		if(amount < 1){
			removeOrder(order);
			return;
		}
		order.setProductsNumber(amount);
		try{
			userDao.open();
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		calculateSum();
	}
	
	public void removeOrder(Order order){
		order.setStatus(OrderStatus.DEFFERED);
		cart.remove(order);
		try{
			userDao.open();
			userDao.updateUser(user);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		calculateSum();
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
			this.totalPrice = String.valueOf(cc) + price;
			this.totalDiscount = String.valueOf(cc) + discount;
		} else {
			this.totalPrice = price + String.valueOf(cc);
			this.totalDiscount = discount + String.valueOf(cc);
		}
	}
	
	public String returnPrice(ProductPrice pPrice, int number){
		Currency productCurrency = pPrice.getCurrency();
		StringBuffer price = new StringBuffer();
		Country code = userCurrency.getCountryCode();
		if(code.isPositionLeft()) price.append(code.getCc());
		double amount = pPrice.getAmount() * number * userCurrency.getKoef() / productCurrency.getKoef();
		price.append(amount);
		if(!code.isPositionLeft()) price.append(code.getCc());
		return price.toString();
	}
	
	public void closeSession(){
		userDao.close();
		categoryDao.close();
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

	public String getTotalPrice() {
		return totalPrice;
	}
	
	public String getTotalDiscount() {
		return totalDiscount;
	}
			
}
