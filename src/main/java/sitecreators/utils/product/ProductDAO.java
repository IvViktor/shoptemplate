/**
 * 
 */
package sitecreators.utils.product;

import java.util.List;

/**
 * @author viktor
 *
 */
public interface ProductDAO {
	
	public Product getProduct(long id);
	
	public List<Product> getProducts();
	
	public List<Product> getProducts(String titleRegExp);
	
	public void addProduct(Product product);
	
	public void updateProduct(Product product);
	
	public void removeProduct(Product product);

}
