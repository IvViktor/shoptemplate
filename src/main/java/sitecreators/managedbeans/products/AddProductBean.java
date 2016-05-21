package sitecreators.managedbeans.products;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.image.Image;
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
	private long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");

	private List<Category> categories = categoryDao.getAllCategories();
	
	private String productTitle;
	
	private int productPrice;
	
	private String description;
	
	private String iconImage = null;
	
	private String imageDesc;
	
	private String category;
	
	private Part imageFile;
	
	private String webappRoot = System.getProperty("catalina.base");
	
	private String defaultIcon = File.separator + "webapps"+ File.separator + "shopImageData"+File.separator + "products" + File.separator + "noimage.gif";
	
	private String noImageDecs = "no_photo";
	
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
		if(iconImage==null){
			iconImage = this.defaultIcon;
			imageDesc = noImageDecs;
		}
		icon.setImagePath(iconImage);
		icon.setImgDecs(imageDesc);
		product.setIcon(icon);
		User owner = userDao.getUser(userId);
		product.setOwner(owner);
		productDao.addProduct(product);
		
		return "home";
	}
	
	public void save() {
		this.iconImage = File.separator + "webapps"+ File.separator + "shopImageData"+File.separator + "products" + File.separator + this.userId + File.separator + getFileName(imageFile);
		try (InputStream input = imageFile.getInputStream()) {
	        Files.copy(input, new File(webappRoot, iconImage).toPath());
	    }
	    catch (IOException e) {
	        // Show faces message?
	    }
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

	public String getIconImage() {
		return iconImage;
	}

	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
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
		
	private String getFileName(Part part) {
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
}
