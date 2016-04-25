package sitecreators.utils.order;

import java.sql.Timestamp;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import sitecreators.utils.product.Product;
import sitecreators.utils.user.User;

@Entity
public class Order {
	
	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	@ManyToOne
	private User customer;
	
	@ManyToOne
	private Product product;
	
	private Timestamp formedTime;
	
	@Embedded
	private OrederStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Timestamp getFormedTime() {
		return formedTime;
	}

	public void setFormedTime(Timestamp formedTime) {
		this.formedTime = formedTime;
	}

	public OrederStatus getStatus() {
		return status;
	}

	public void setStatus(OrederStatus status) {
		this.status = status;
	}
	
}
