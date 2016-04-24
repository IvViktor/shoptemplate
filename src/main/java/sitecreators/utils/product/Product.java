/**
 * 
 */
package sitecreators.utils.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import sitecreators.utils.category.Category;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.user.User;

/**
 * @author User
 *
 */
@Entity
public class Product {

	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	private String productTitle;
	
	@Embedded
	private ProductPrice productPrice;
	
	@ManyToOne
	private Category category;
	
	@ManyToOne
	private User owner;
	
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<User> customers = new ArrayList<>();
	
	@Embedded
	private ProductDecription description;
	
	@OneToOne
	@JoinColumn(name="icon_image_id")
	private Image icon;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public ProductPrice getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getCustomers() {
		return customers;
	}

	public void addCustomer(User customer){
		this.customers.add(customer);
		customer.getPurchases().add(this);
	}
	
	public void removeCustomer(User customer){
		this.customers.remove(customer);
		customer.getPurchases().remove(this);
	}
	
	public ProductDecription getDescription() {
		return description;
	}

	public void setDescription(ProductDecription description) {
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

	public void addImage(Image image){
		this.images.add(image);
	}
	
	public void removeImage(Image image){
		this.images.remove(image);
	}
	
	public List<Comment> getComments() {
		return comments;
	}

	public void addComment(Comment comment){
		this.comments.add(comment);
	}
	
	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}
}