/**
 * 
 */
package sitecreators.utils.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import sitecreators.utils.auth.Password;
import sitecreators.utils.comment.Comment;
import sitecreators.utils.image.Image;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderStatus;
import sitecreators.utils.product.Product;

/**
 * @author User
 *
 */
@Entity
public class User {
	
	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	@Embedded
	private UserAbout about;
		
	@Embedded
	private UserContacts contacts;
	
	@OneToOne
	@JoinColumn(name="pswd_id")
	private Password password;
	
	@OneToOne
	@JoinColumn(name="icon_image_id")
	private Image icon;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();
	
	@OneToMany(mappedBy="publisher",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> selling = new ArrayList<>();
	
	@OneToMany(mappedBy="customer",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> purchases = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
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

	public List<Product> getSelling() {
		return selling;
	}

	public void addSellProduct(Product product){
		this.selling.add(product);
		product.setOwner(this);
	}

	public void removeSellProduct(Product product){
		this.selling.remove(product);
		product.setOwner(null);
	}
	
	public List<Order> getPurchases() {
		return purchases;
	}
	
	public void addPurchase(Order order){
		this.purchases.add(order);
		order.setCustomer(this);
	}
	
	public void removePurchase(Order order, OrderStatus status){
		order.setStatus(status);
	}

	public UserAbout getAbout() {
		return about;
	}

	public void setAbout(UserAbout about) {
		this.about = about;
	}

	public UserContacts getContacts() {
		return contacts;
	}

	public void setContacts(UserContacts contacts) {
		this.contacts = contacts;
	}
}
