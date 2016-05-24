package sitecreators.managedbeans.orders;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import sitecreators.utils.ApplicationContextUtil;
import sitecreators.utils.order.Order;
import sitecreators.utils.order.OrderDAO;
import sitecreators.utils.user.User;
import sitecreators.utils.user.UserDAO;

@ManagedBean(name="orderListBean")
public class OrderListBean {
	
	private OrderDAO orderDao;
	
	private List<Order> orders;
	
	
	public OrderListBean(){
		orderDao = (OrderDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
		orders = orderDao.getOrders();
		long userId =(long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userID");
		UserDAO userDao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
		User user = userDao.getUser(userId);
	}

	public List<Order> getOrders() {
		return orders;
	}


	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
}
