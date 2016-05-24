package sitecreators.managedbeans.products;


import java.sql.Timestamp;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.order.Order;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="prodDetailBean")
public class ProductDetailsBean {
	
	private String productId;
	
	private ProductDAO productDao;
	
	private Product product;
	
	private User user;
	
	private UserDAO userDao;
	
	public ProductDetailsBean(){
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		productId=(String) req.getParameter("productId");
		this.product = productDao.getProduct(Long.parseLong(this.productId));
		long userId = 1;//(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		//user = userDao.getUser(userId);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void addComment(String body){
		Comment comment = new Comment();
		comment.setBody(body);
		//comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		this.product.addComment(comment);
		productDao.updateProduct(product);
	}
	
	public void buyProduct(){
		Order order = new Order();
		order.setFormedTime(new Timestamp(new Date().getTime()));
		product.addOrder(order);
		//user.addPurchase(order);
		productDao.updateProduct(product);
		//userDao.updateUser(user);
	}

}
