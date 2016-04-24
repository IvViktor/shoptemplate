package sitecreators.utils.product;

import javax.persistence.Embeddable;

@Embeddable
public class ProductDecription {
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
