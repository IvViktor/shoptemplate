package sitecreators.managedbeans;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;

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

}
