/**
 * 
 */
package sitecreators.utils.user;

import java.util.List;

/**
 * @author viktor
 *
 */
public interface UserDAO {
	
	public User getUser(long id);
	
	public List<User> getUsers();
	
	public List<User> getUsers(String namePattern);
	
	public void addUser(User user);
	
	public void updateUser(User user);
	
	public void removeUser(User user);
	
}
