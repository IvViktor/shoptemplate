package sitecreators.managedbeans.bootshop;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.util.Precision;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="categoryEditorBean")
public class CategoryEditorBean {
	
	private User user;

	private UserDAO userDao;
	
	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private CategoryDAO categoryDao;
	
	private List<Category> categories;
	
	private Category selectedCategory;

	private String totalPrice;

	private Currency userCurrency;
	
	private String newCategory;
	
		
	public CategoryEditorBean(){
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
			calculateSum();

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
			this.totalPrice = String.valueOf(cc) + Precision.round(price, 2);
		} else {
			this.totalPrice = Precision.round(price, 2) + String.valueOf(cc);
		}
	}
			
	public void closeSession(){
		userDao.close();
		categoryDao.close();
	}
	
	public void add(){
		for(Category cat : categories){
			if(newCategory.equals(cat.getTitle())) return;
		}
		Category category = new Category();
		category.setTitle(newCategory);
		categoryDao.open();
		categoryDao.addCategory(category);
		categoryDao.close();
		this.categories.add(category);
		newCategory = "";
	}
	
	public void remove(){
		categories.remove(selectedCategory);
		categoryDao.open();
		categoryDao.removeCategory(selectedCategory);
		categoryDao.close();
	}
	
	public String update(Category category){
		categories.remove(category);
		category.setTitle(newCategory);
		categories.add(category);
		categoryDao.open();
		categoryDao.updateCategory(category);
		categoryDao.close();
		newCategory = "";
		
		return "categoryeditor";
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

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}



	public Category getSelectedCategory() {
		return selectedCategory;
	}



	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}



	public String getNewCategory() {
		return newCategory;
	}



	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}
		
}
