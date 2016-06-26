package sitecreators.managedbeans.bootshop;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.finance.Currency;
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

@ManagedBean(name="editProductBean")
public class ProductEditorBean {
	
	private CategoryDAO categoryDao;
	
	private ProductDAO productDao;
	
	private UserDAO userDao;
	
	private ImageDAO imageDao;
	
	private long userId;
	
	private User user;
	
	private String userName;
	
	private List<Order> cart = new ArrayList<>();
	
	private String totalPrice;
	
	private List<Category> categories;
	
	private Product product;

	private String productId;
	
	private String title;
	
	private double price;
	
	private String category;
	
	private String description;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;

	private Part imageFile;
	
	private String imageDesc;
	
	private Image newIcon;
	
	private Comment deletedComment;
	
	private ExternalContext exc;
	
	private ProductStatus[] statusList;
	
	private String selectedStatus;

	private Currency userCurrency;
	
	
	
	public ProductEditorBean() throws Exception{
		categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		try{
			userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		} catch (Exception e){
			userId = 0;
		}
		if(userId == 0) throw new Exception("Unauthorised user");
		exc = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) exc.getRequest();
		productId=(String) req.getParameter("productId");
		long prodId = 0;
		try{
			prodId = Long.parseLong(productId);
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("unknown product");
		}
		try{
			productDao.open();
			this.product = productDao.getProduct(prodId);
			this.title = product.getProductTitle();
			ProductPrice pPrice = product.getProductPrice();
			this.price = pPrice.getAmount();
			ProductDecription pDescr = this.product.getDescription();
			if(pDescr != null) this.description = pDescr.getDescription();
			Category tempcat = product.getCategory();
			if(tempcat != null) this.category = tempcat.getTitle();
			this.icon = product.getIcon();
			this.images = product.getImages();
			this.comments = product.getComments();
		}catch (Exception e){
			e.printStackTrace();
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
			categoryDao.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		calculateSum();
		statusList = ProductStatus.values();
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
	
	public void closeSession(){
		productDao.close();
		userDao.close();
		categoryDao.close();
	}
	
	public void addImage(){
		Image newImage = new Image();
		String filePath = imageDao.saveImage(imageFile, "products",this.user.getId());//
		newImage.setImagePath(filePath);
		newImage.setImgDecs(imageDesc);
		imageDao.addImage(newImage);
		product.addImage(newImage);
		try{
			this.productDao.open();
			this.productDao.updateProduct(this.product);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
		this.productDao.close();
		}
	}
	
	public void setNewIcon(){
		product.setIcon(newIcon);
	}
	
	private void changeCategory(String title){
		try{
			categoryDao.open();
			Category cat = categoryDao.getCategory(title);
			categoryDao.close();
			if(cat != null) product.setCategory(cat);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void removeComment(){
		product.removeComment(deletedComment);
	}
	
	public String save(){
		changeCategory(category);
		ProductStatus status = ProductStatus.valueOf(selectedStatus);
		product.setStatus(status);
		product.setProductTitle(title);
		ProductPrice pPrice = product.getProductPrice();
		if(pPrice == null) pPrice = new ProductPrice();
		pPrice.setAmount(price);
		ProductDecription pDescr = product.getDescription();
		if (pDescr == null) pDescr = new ProductDecription();
		pDescr.setDescription(description);
		try{
			this.productDao.open();
			this.productDao.updateProduct(this.product);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
		this.productDao.close();
		}
		return "home";
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Part getImageFile() {
		return imageFile;
	}

	public void setImageFile(Part imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public Image getNewIcon() {
		return newIcon;
	}

	public void setNewIcon(Image newIcon) {
		this.newIcon = newIcon;
	}

	public Comment getDeletedComment() {
		return deletedComment;
	}

	public void setDeletedComment(Comment deletedComment) {
		this.deletedComment = deletedComment;
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

			
}
