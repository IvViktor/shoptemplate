/**
 * 
 */
package sitecreators.utils.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author User
 *
 */
@Entity
public class Password {

	@Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
	private long id;
	
	private String salt;
	
	private String passwordHex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPasswordHex() {
		return passwordHex;
	}

	public void setPasswordHex(String passwordHex) {
		this.passwordHex = passwordHex;
	}
	
	
}
