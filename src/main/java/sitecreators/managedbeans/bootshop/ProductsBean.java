package sitecreators.managedbeans.bootshop;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math3.util.Precision;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.auth.Password;
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

@ManagedBean(name="productsBean")
public class ProductsBean {
	
	private User user;

	private UserDAO userDao; 

	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Product> products;
	
	private int productsNumber;
	
	private String sortType;
	
	private List<Category> categories;
	
	private String loginEmail;
	
	private String loginPassword;
	
	private String selectedCategory;
	
	private String minPrice;
	
	private String maxPrice;
	
	private String searchText = null;
	
	private String prodPerPage;
	
	private int pageNum;
	
	private int totalPages;

	private String totalPrice;

	private Currency userCurrency;
	
	public ProductsBean(){
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		this.categoryDao =(CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		try{
			userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (NullPointerException e){
			userId = 0;
		}
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.prodPerPage = (String) req.getParameter("ppp");
		try{
			String page = (String) req.getParameter("page");
			this.pageNum = Integer.parseInt(page);
		} catch (Exception e){
			pageNum = 1;
		}
		if(pageNum == 0) pageNum++;
		this.minPrice = (String) req.getParameter("minprice");
		this.maxPrice = (String) req.getParameter("maxprice");
		this.selectedCategory = (String) req.getParameter("selectedCategory");
		this.sortType = (String) req.getParameter("sortBy");
		this.searchText = (String) req.getParameter("searchText");
		int ppp = 6;
		int page = pageNum;
		int productAmount = 0;
		try{
			ppp = Integer.parseInt(prodPerPage);
		} catch (Exception e){ 
			ppp=6;
			prodPerPage = "6";
		}
		Category filterCategory = null;
		double min=0 ,max=0;
		try{
			productDao.open();
			if(selectedCategory != null){
				categoryDao.open();
				filterCategory = categoryDao.getCategory(selectedCategory);
			}
			try{
				max = Double.parseDouble(maxPrice);
				System.out.println("parsed maxPrice "+max);
			} catch (Exception e){ max = 0;}
			try{
				min = Double.parseDouble(minPrice);
				System.out.println("parsed minPrice"+min);
			} catch (Exception e){ min = 0;}
			if((searchText == null)||(searchText.length() == 0)){
				productAmount = ((Number) productDao.getProductsNumber(filterCategory, min, max)).intValue();
				this.products = productDao.getProducts(filterCategory,min,max,(page-1)*ppp,ppp);
			} else {
				this.products = productDao.getProducts(searchText,filterCategory);
				productAmount = ((Number) productDao.getProductsNumber(searchText,filterCategory)).intValue();
			}
			this.productsNumber = productAmount;
			this.totalPages = productAmount / ppp;
			if((productAmount % ppp) > 0) totalPages++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			productDao.close();
			categoryDao.close();
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
		
		if(sortType == null) sortType="";
		if(sortType.equalsIgnoreCase("nasc")){
			products.sort((Product p1, Product p2) -> p1.getProductTitle().compareTo(p2.getProductTitle()));
		} else if(sortType.equalsIgnoreCase("ndesc")){
			products.sort((Product p1, Product p2) -> (p1.getProductTitle().compareTo(p2.getProductTitle()))*(-1));
		} else if(sortType.equalsIgnoreCase("lpf")){
			products.sort((Product p1,Product p2) ->(int)( p1.getProductPrice().getAmount() - p2.getProductPrice().getAmount()));
		} else {
			products.sort((Product p1,Product p2) -> (int)(p1.getId() - p2.getId())*(-1));
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
	
	public String returnPrice(ProductPrice pPrice){
		Currency productCurrency = pPrice.getCurrency();
		StringBuffer price = new StringBuffer();
		if(userCurrency == null) userCurrency = productCurrency;
		Country code = userCurrency.getCountryCode();
		if(code.isPositionLeft()) price.append(code.getCc());
		double amount = 0;
		try{
			amount = pPrice.getAmount() * userCurrency.getKoef() / productCurrency.getKoef();
		} catch (NullPointerException e){
			amount = pPrice.getAmount();
		}
		
		price.append(Precision.round(amount, 2));
		if(!code.isPositionLeft()) price.append(code.getCc());
		return price.toString();
	}
	
	private Order checkOrder(Product product){
		for(Order order : cart){
			if(order.getProduct().getId() == product.getId()) return order;
		}
		return null;
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
				this.userCurrency = user.getCurrency();
				calculateSum();

			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
	}
	
	public void closeSession(){
		productDao.close();
		userDao.close();
		categoryDao.close();
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

	public int getProductsNumber() {
		return productsNumber;
	}

	public void setProductsNumber(int productsNumber) {
		this.productsNumber = productsNumber;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
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

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getProdPerPage() {
		return prodPerPage;
	}

	public void setProdPerPage(String prodPerPage) {
		this.prodPerPage = prodPerPage;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
