package sitecreators.utils.user;

import javax.persistence.Embeddable;

@Embeddable
public class UserContacts {

	private String pnoneNumber;
	
	private String email;

	public String getPnoneNumber() {
		return pnoneNumber;
	}

	public void setPnoneNumber(String pnoneNumber) {
		this.pnoneNumber = pnoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
