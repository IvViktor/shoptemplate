package sitecreators.managedbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.category.Category;
import sitecreators.utils.category.CategoryDAO;
import sitecreators.utils.finance.Currency;
import sitecreators.utils.finance.CurrencyDAO;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;


@ManagedBean(name="indexBean")
public class HomeBean {
	
	CategoryDAO categorydao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
	ProductDAO productdao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
	UserDAO userdao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
	CurrencyDAO currencydao = (CurrencyDAO) ApplicationContextUtil.getApplicationContext().getBean("CurrencyDAO");
	OrderDAO orderdao = (OrderDAO) ApplicationContextUtil.getApplicationContext().getBean("OrderDAO");
	List<Category> categories = categorydao.getAllCategories();
	List<Currency> currencies = currencydao.getAllCurrencies();
	List<Order> orders = orderdao.getOrders();
	List<Product> products = productdao.getProducts();
	List<User> users = userdao.getUsers();

	public int getCategoriesNumber(){
		return categories.size();
	}
	
	public int getCurrenciesNumber(){
		return currencies.size();
	}
	
	public int getOrdersNumber(){
		return orders.size();
	}
	
	public int getProductsNumber(){
		return products.size();
	}
	
	public int getUsersNumber(){
		return users.size();
	}
}
