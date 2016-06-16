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
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
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
	
	private String pageNum;
	
	private int totalPages;
	
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
		this.pageNum = (String) req.getParameter("page");
		this.minPrice = (String) req.getParameter("minprice");
		this.maxPrice = (String) req.getParameter("maxprice");
		this.selectedCategory = (String) req.getParameter("category");
		this.sortType = (String) req.getParameter("sortBy");
		int ppp = 6;
		int page = 1;
		int productAmount = 0;
		try{
			ppp = Integer.parseInt(prodPerPage);
		} catch (Exception e){ 
			ppp=6;
			prodPerPage = "6";
		}
		if(pageNum != null) page = Integer.parseInt(pageNum);
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
			if(searchText == null){
				productAmount = ((Number) productDao.getProductsNumber(filterCategory, min, max)).intValue();
				this.products = productDao.getProducts(filterCategory,min,max,(page-1)*ppp,ppp);
			} else {
				this.products = productDao.getProducts(searchText);
				productAmount = ((Number) productDao.getProductsNumber(searchText)).intValue();
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
		} catch (Exception e){
			e.printStackTrace();
		}
		if(sortType == null) sortType="";
		if(sortType.equalsIgnoreCase("nasc")){
			products.sort((Product p1, Product p2) -> p1.getProductTitle().compareTo(p2.getProductTitle()));
		} else if(sortType.equalsIgnoreCase("ndesc")){
			products.sort((Product p1, Product p2) -> (p1.getProductTitle().compareTo(p2.getProductTitle()))*(-1));
		} else if(sortType.equalsIgnoreCase("lpf")){
			products.sort((Product p1,Product p2) -> p1.getProductPrice().getAmount() - p2.getProductPrice().getAmount());
		} else {
			products.sort((Product p1,Product p2) -> (int)(p1.getId() - p2.getId())*(-1));
		}
		
	}
	
	public void addToCart(String id){
		long productId = Long.parseLong(id);
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

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
