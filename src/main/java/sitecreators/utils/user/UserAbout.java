package sitecreators.utils.user;

import javax.persistence.Embeddable;

@Embeddable
public class UserAbout {

	private String firstName;
	
	private String secondName;
	
	private String aboutInfo;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getAboutInfo() {
		return aboutInfo;
	}

	public void setAboutInfo(String aboutInfo) {
		this.aboutInfo = aboutInfo;
	}
	
}
