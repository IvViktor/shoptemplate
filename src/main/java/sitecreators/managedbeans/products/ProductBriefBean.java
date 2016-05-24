package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;

import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;

@ManagedBean(name="productPanelBean")
public class ProductBriefBean {
	
	private ProductDAO productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
	
	private List<Product> products;
	
	public String details(){
		return "productdetails";
	}

	public List<Product> getProducts() {
		this.products = productDao.getProducts();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
