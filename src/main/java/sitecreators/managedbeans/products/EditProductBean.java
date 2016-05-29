package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.image.ImageDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductDecription;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="editProductBean")
public class EditProductBean {
	
	private CategoryDAO categoryDao;
	
	private ProductDAO productDao;
	
	private UserDAO userDao;
	
	private ImageDAO imageDao;
	
	private long userId =1;
	
	private List<Category> categories;
	
	private Product product;

	private String productId;
	
	private String title;
	
	private int price;
	
	private String category;
	
	private String description;
	
	private Image icon;
	
	private List<Image> images;
	
	private List<Comment> comments;

	private User user;
	
	private Part imageFile;
	
	private String imageDesc;
	
	
	
	public EditProductBean(){
		categoryDao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		imageDao = (ImageDAO) ApplicationContextUtil.getApplicationContext().getBean("ImageDAO");
		//userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		productId=(String) req.getParameter("productId");
		try{
			productDao.open();
			this.product = productDao.getProduct(Long.parseLong(productId));
			this.title = product.getProductTitle();
			ProductPrice pPrice = product.getProductPrice();
			this.price = pPrice.getAmount();
			ProductDecription pDescr = this.product.getDescription();
			if(pDescr != null) this.description = pDescr.getDescription();
			//this.category = this.product.getCategory().getTitle();
			this.icon = product.getIcon();
			this.images = product.getImages();
			this.comments = product.getComments();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		try{	
			categoryDao.open();
			this.categories = categoryDao.getAllCategories();
			categoryDao.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		/*try{
			userDao.open();
			this.user = userDao.getUser(userId);
			userDao.close();
		}catch (Exception e){
			e.printStackTrace();
		}*/
		
	}
	
	public void closeSession(){
		productDao.close();
	}
	
	public void addImage(){
		Image newImage = new Image();
		String filePath = imageDao.saveImage(imageFile, "products",this.userId);//this.user.getId()
		newImage.setImagePath(filePath);
		newImage.setImgDecs(imageDesc);
		imageDao.addImage(newImage);
		images.add(newImage);
	}
	
	public void setNewIcon(Image newIcon){
		images.add(icon);
		product.setIcon(newIcon);
		images.remove(newIcon);
	}
	
	public void changeCategory(String title){
		categoryDao.open();
		Category cat = categoryDao.getCategory(title);
		categoryDao.close();
		product.setCategory(cat);
	}
	
	public void removeComment(Comment comment){
		comments.remove(comment);
	}
	
	public String save(){
		try{
			this.productDao.open();
			this.productDao.updateProduct(this.product);
		} catch (Exception e){
			
		} finally{
		this.productDao.close();
		}
		return "products";
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

			
}
