/**
 * 
 */
package sitecreators.utils.product;

import java.util.List;

import sitecreators.utils.category.Category;

/**
 * @author viktor
 *
 */
public interface ProductDAO {
	
	public Product getProduct(long id);
	
	public Number getDiscountProductsNumber();
	
	public Number getProductsNumber(Category category, double minPrice, double maxPrice);
	
	public Number getProductsNumber(String titleRegExp,Category category);

	public List<Product> getProducts();
	
	public List<Product> getFeaturedProducts();
	
	public List<Product> getLatestProducts();
	
	public List<Product> getDiscountProducts(int start,int end);

	public List<Product> getProducts(int startNum, int length);
	
	public List<Product> getProducts(String titleRegExp,Category category);
	
	public List<Product> getProducts(Category category,double minPrice,double maxPrice,int startNum, int length);
	
	public void addProduct(Product product);
	
	public void updateProduct(Product product);
	
	public void removeProduct(Product product);
	
	public void open();
	
	public void close();

}
