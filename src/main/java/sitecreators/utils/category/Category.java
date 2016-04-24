/**
 * 
 */
package sitecreators.utils.category;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import sitecreators.utils.product.Product;

/**
 * @author User
 *
 */
@Entity
public class Category {
	
	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	@NaturalId
	private String title;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void addProduct(Product product){
		products.add(product);
		product.setCategory(this);
	}
	
	public void removeProduct(Product product){
		products.remove(product);
		product.setCategory(null);
	}
	
}
