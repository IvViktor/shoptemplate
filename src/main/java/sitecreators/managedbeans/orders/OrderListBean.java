package sitecreators.managedbeans.orders;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderDAO;
import sitecreators.utils.product.Product;
import sitecreators.utils.product.ProductDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="orderListBean")
public class OrderListBean {
	
	private OrderDAO orderDao;
	
	private UserDAO userDao;
	
	private ProductDAO productDao;
	
	private List<Order> orders;
	
	private Product product = null;
	
	private User user, customer = null;
	
	
	public OrderListBean(){
		userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		orderDao = (OrderDAO) ApplicationContextUtil.getApplicationContext().getBean("OrderDAO");
		productDao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
		long userId = 1;//(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String productId = (String) req.getParameter("productId");
		String customerId = (String) req.getParameter("customerId");
		try{
		if(productId != null){
			productDao.open();
			this.product = productDao.getProduct(Long.parseLong(productId));
			orders = product.getOrders();
		} else if(customerId != null){
			userDao.open();
			customer = userDao.getUser(Long.parseLong(customerId));
			orders = customer.getPurchases();
		} else {
			userDao.open();
			user = userDao.getUser(userId);
			if(userId == 1){
				orders = orderDao.getOrders();
			} else orders = user.getPurchases();
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void closeSession(){
		productDao.close();
		userDao.close();
	}
	
	
	public List<Order> getOrders() {
		return orders;
	}


	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
}
