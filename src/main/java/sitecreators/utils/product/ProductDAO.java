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
	
	public List<Product> getProducts(int startNum, int length);
	
	public List<Product> getProducts(String titleRegExp);
	
	public List<Product> getProducts(Category category,double minPrice,double maxPrice);
	
	public void addProduct(Product product);
	
	public void updateProduct(Product product);
	
	public void removeProduct(Product product);
	
	public void open();
	
	public void close();

}
