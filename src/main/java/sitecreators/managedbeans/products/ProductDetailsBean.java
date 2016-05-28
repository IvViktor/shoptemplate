package sitecreators.managedbeans.products;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.order.Order;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductDecription;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="prodDetailBean")
public class ProductDetailsBean {
	
	private String productId;
	
	private ProductDAO productDao;
	
	private Product product;
	
	private User user;
	
	private UserDAO userDao;
	
	private String title;
	
	private int price;
	
	private String category;
	
	private String description;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;
	
	public ProductDetailsBean(){
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		productId=(String) req.getParameter("productId");
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		try{
			productDao.open();
			this.product = productDao.getProduct(Long.parseLong(this.productId));
			this.title = product.getProductTitle();
			ProductPrice pPrice = product.getProductPrice();
			this.price = pPrice.getAmount();
			//this.category = product.getCategory().getTitle();
			ProductDecription pDescr = product.getDescription();
			this.description = pDescr.getDescription();
			this.icon = product.getIcon();
			this.images = product.getImages();
			this.comments = product.getComments();
		} catch (Exception e){
			e.printStackTrace();
			productDao.close();
		}/* finally{
			productDao.close();
		}*/
		//long userId = 1;//(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		//userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		//user = userDao.getUser(userId);
	}
	
	public void closeSession(){
		productDao.close();
	}

	public void addComment(String body){
		Comment comment = new Comment();
		comment.setBody(body);
		//comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		this.product.addComment(comment);
		try{
			productDao.open();
			productDao.updateProduct(product);
		} catch (Exception e){
			
		} finally {
			productDao.close();
		}
	}
	
	public void buyProduct(){
		Order order = new Order();
		order.setFormedTime(new Timestamp(new Date().getTime()));
		product.addOrder(order);
		//user.addPurchase(order);
		try{
			productDao.open();
			productDao.updateProduct(product);
		} catch (Exception e){
			
		} finally {
			productDao.close();
		}		
		//userDao.updateUser(user);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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

}
