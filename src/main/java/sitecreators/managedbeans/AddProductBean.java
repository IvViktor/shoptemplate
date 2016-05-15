package sitecreators.managedbeans;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.product.ProductPrice;
import sitecreators.utils.user.User;

@ManagedBean(name="addProductBean")
public class AddProductBean {

	private String productTitle;
	
	private String category;
	
	private int productPrice;
	
	ProductDAO productdao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
	CategoryDAO categorydao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	
	public String addProduct() {
		Product product = new Product();
		Category cat = new Category();
		cat.setTitle(this.category);
		product.setProductTitle(productTitle);
		product.setCategory(cat);
		ProductPrice pPrice = new ProductPrice();
		pPrice.setAmount(productPrice);
		product.setProductPrice(pPrice);
		
		categorydao.addCategory(cat);
		productdao.addProduct(product);
				
		return "home";
	}
}
