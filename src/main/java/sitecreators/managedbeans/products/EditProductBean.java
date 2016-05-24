package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="editProductBean")
public class EditProductBean {
	
	private CategoryDAO categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
	private ProductDAO productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
	private UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
	private ImageDAO imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
	private long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
	
	private List<Category> categories = categoryDao.getAllCategories();
	
	private Product product;
	
	private User user;
	
	@ManagedProperty(value="#{param.ownerId}")
	private String productId;
	
	public EditProductBean(){
		this.product = productDao.getProduct(Long.parseLong(productId));
		this.user = userDao.getUser(userId);
	}
	
	public void addImage(Part imageFile, String imageDesc){
		Image newImage = new Image();
		String filePath = imageDao.saveImage(imageFile, "products", this.user.getId());
		newImage.setImagePath(filePath);
		newImage.setImgDecs(imageDesc);
		imageDao.addImage(newImage);
		product.getImages().add(newImage);
	}
	
	public void setNewIcon(Image newIcon){
		product.getImages().add(product.getIcon());
		product.setIcon(newIcon);
		product.getImages().remove(newIcon);
	}
	
	public void save(){
		this.productDao.updateProduct(this.product);
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
		
}
