package sitecreators.managedbeans.products;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;

import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;

@ManagedBean(name="productPanelBean")
public class ProductBriefBean {
	
	private ProductDAO productDao;
	
	private List<Product> products;
	
	public ProductBriefBean(){
		this.productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		try{
			productDao.open();
			this.products = productDao.getProducts(0,20);
		} catch (Exception e) {
			
		} finally{
			productDao.close();
		}
	}
	
	public String details(){
		return "productdetails";
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
