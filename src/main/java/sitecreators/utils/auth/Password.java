/**
 * 
 */
package sitecreators.utils.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

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
	
	public void create(String password) throws NoSuchAlgorithmException{
		this.salt = UUID.randomUUID().toString();
		String saltAndPass = this.salt+password;
		MessageDigest md = null;
		md = MessageDigest.getInstance("SHA-1");
		md.reset();
		md.update(saltAndPass.getBytes());
		this.passwordHex = this.byteToHex(md.digest());
	}
	
	public boolean check(String password) throws NoSuchAlgorithmException{
		String saltAndPass = this.salt+password;
		MessageDigest md = null;
		md = MessageDigest.getInstance("SHA-1");
		md.reset();
		md.update(saltAndPass.getBytes());
		password = this.byteToHex(md.digest());
		if(password.equals(this.passwordHex)) return true;
		return false;
	}
		
	private String byteToHex(final byte[] hash){
	    Formatter formatter = new Formatter();
	    for (byte b : hash){
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
