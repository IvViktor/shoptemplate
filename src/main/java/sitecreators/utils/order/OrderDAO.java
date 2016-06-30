/**
 * 
 */
package sitecreators.utils.order;

import java.util.List;

/**
 * @author viktor
 *
 */
public interface OrderDAO {

	public List<Order> getOrders();
	
	public void update(Order order);
	
}
