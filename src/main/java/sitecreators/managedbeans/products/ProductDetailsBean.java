package sitecreators.managedbeans.products;


import java.sql.Timestamp;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="prodDetailBean")
public class ProductDetailsBean {
	
	@ManagedProperty(value="#{param.productId}")
	private String productId;
	
	private ProductDAO productDao;
	
	private Product product;
	
	private ProductDetailsBean(){
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		this.product = productDao.getProduct(Long.parseLong(this.productId));
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public void addComment(String body){
		long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		User user = userDao.getUser(userId);
		Comment comment = new Comment();
		comment.setBody(body);
		comment.setPublisher(user);
		comment.setPublishTime(new Timestamp(new Date().getTime()));
		this.product.addComment(comment);
		productDao.updateProduct(product);
	}

}
