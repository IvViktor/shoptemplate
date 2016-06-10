package sitecreators.managedbeans.bootshop;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.order.Order;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="indexBean")
public class IndexBean {
	
	private User user;

	private UserDAO userDao; 

	private long userId = 0;
	
	private String userName;
	
	private List<Order> cart;
	
	private ProductDAO productDao;
	
	private CategoryDAO categoryDao;
	
	private List<Product> products;
	
	private List<Product> featuredProducts;

	private String selectedCategory;
	
	private String minPrice;
	
	private String maxPrice;
	
	private String searchText = null;
	
	private List<Category> categories;
	
	private String prodPerPage;

	private String loginEmail;
	
	private String loginPassword;
}
