package sitecreators.utils.order;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import sitecreators.utils.product.Product;
import sitecreators.utils.user.User;

@Entity
@Table(name = "Order_table")
public class Order {
	
	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	@Column(name = "`id`")
	private long id;
	
	@ManyToOne
	private User customer;
	
	@ManyToOne
	private Product product;
	
	private Timestamp formedTime;
	
	private int productsNumber = 1;
	
	@Enumerated(EnumType.STRING)
	@Column(name="`order_status`")
	private OrderStatus status;

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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getProductsNumber() {
		return productsNumber;
	}

	public void setProductsNumber(int productsNumber) {
		this.productsNumber = productsNumber;
	}
	
}
