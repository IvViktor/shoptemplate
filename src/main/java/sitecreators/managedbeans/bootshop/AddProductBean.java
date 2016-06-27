package sitecreators.managedbeans.bootshop;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.apache.commons.math3.util.Precision;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.finance.Country;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.finance.CurrencyDAO;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductDecription;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.product.ProductStatus;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserAbout;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="addProductBean")
public class AddProductBean {
	
	private CategoryDAO categoryDao;
	private ProductDAO productDao;
	private UserDAO userDao;
	private ImageDAO imageDao;
	private CurrencyDAO currencyDao;
	
	private long userId;
	
	private User user;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private String totalPrice;
	
	private List<Category> categories;
	
	private String productTitle;
	
	private double productPrice;
	
	private String description;
	
	private String iconImage;
	
	private String imageDesc = "no image";
	
	private String category;
	
	private Part imageFile;
	
	private ProductStatus[] statusList;
	
	private String selectedStatus;
	
	private Currency userCurrency;
	
	private List<Currency> currencies = new ArrayList<>();

	private String productCurrency;
	
	private List<String> currencyCodes = new ArrayList<>();
	
	private double discount;
	
	public AddProductBean() throws Exception{
		
		categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		currencyDao = (CurrencyDAO) ApplicationContextUtil.getApplicationContext().getBean("CurrencyDAO");
		try{
			userId = (long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (Exception e){
			userId = 0;
		}
		if(userId == 0) throw new Exception("Unauthorised user");
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
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			userDao.close();
		}
		calculateSum();
		statusList = ProductStatus.values();
		this.currencies = currencyDao.getAllCurrencies();
		for(Country c : Country.values()){
			currencyCodes.add(c.getStringValue());
		}
	}
	
	public void closeSession(){
		categoryDao.close();
		userDao.close();
		productDao.close();
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
			this.totalPrice = Precision.round(price, 2) + String.valueOf(cc);
		}
	}
	
	public String addProduct(){
		Product product = new Product();
		product.setProductTitle(productTitle);
		ProductPrice pPrice = new ProductPrice();
		pPrice.setAmount(productPrice);
		pPrice.setCurrency(this.getCurrency(productCurrency));
		pPrice.setDiscount(discount);
		product.setProductPrice(pPrice);
		ProductStatus status = ProductStatus.valueOf(selectedStatus);
		product.setStatus(status);
		
		try{
			categoryDao.open();
			Category cat = categoryDao.getCategory(category);
			if(cat != null) product.setCategory(cat);
			categoryDao.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		ProductDecription pDescr = new ProductDecription();
		pDescr.setDescription(description);
		product.setDescription(pDescr);
		Image icon = new Image();
		iconImage = imageDao.saveImage(imageFile, "products", this.userId);
		icon.setImagePath(iconImage);
		icon.setImgDecs(imageDesc);
		imageDao.addImage(icon);
		product.setIcon(icon);
		if(imageFile != null) product.addImage(icon);
		try{
			productDao.open();
			productDao.addProduct(product);
			product.setOwner(user);
			productDao.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return "products";
	}
	
	private Currency getCurrency(String code){
		for(Currency c : currencies){
			if(c.getCountryCode().getStringValue().equals(code)) return c;
		}
		return getCurrency("USD");
	}
	
	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Part getImageFile() {
		return imageFile;
	}

	public void setImageFile(Part imageFile) {
		this.imageFile = imageFile;
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

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ProductStatus[] getStatusList() {
		return statusList;
	}

	public void setStatusList(ProductStatus[] statusList) {
		this.statusList = statusList;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}

	public String getProductCurrency() {
		return productCurrency;
	}

	public void setProductCurrency(String productCurrency) {
		this.productCurrency = productCurrency;
	}

	public List<String> getCurrencyCodes() {
		return currencyCodes;
	}

	public void setCurrencyCodes(List<String> currencyCodes) {
		this.currencyCodes = currencyCodes;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
			
}
