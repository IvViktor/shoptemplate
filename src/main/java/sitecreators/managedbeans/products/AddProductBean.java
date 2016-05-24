package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductDecription;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="addProductBean")
public class AddProductBean {
	
	private CategoryDAO categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
	private ProductDAO productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
	private UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
	private ImageDAO imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
	private long userId = 1;//(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
	private List<Category> categories = categoryDao.getAllCategories();
	
	private String productTitle;
	
	private int productPrice;
	
	private String description;
	
	private String iconImage;
	
	private String imageDesc;
	
	private String category;
	
	private Part imageFile;
	
	public String addProduct(){
		Product product = new Product();
		product.setProductTitle(productTitle);
		ProductPrice pPrice = new ProductPrice();
		pPrice.setAmount(productPrice);
		product.setProductPrice(pPrice);
		Category cat = categoryDao.getCategory(category);
		product.setCategory(cat);
		ProductDecription pDescr = new ProductDecription();
		pDescr.setDescription(description);
		Image icon = new Image();
		iconImage = imageDao.saveImage(imageFile, "products", this.userId);
		icon.setImagePath(iconImage);
		icon.setImgDecs(imageDesc);
		imageDao.addImage(icon);
		product.setIcon(icon);
		//User owner = userDao.getUser(userId);
		//product.setOwner(owner);
		productDao.addProduct(product);
		
		return "products";
	}
	
	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
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
		
}