/**
 * 
 */
package sitecreators.utils.product;

/**
 * @author User
 *
 */
public enum ProductStatus {
	
	INSTOCK("INSTOCK","In stock"),
	SALED("SALED","Saled"),
	FEATURED("FEATURED","Featured"),
	DISCOUNT("DISCOUNT","Discount"),
	CARUSEL("CARUSEL","On index page");
	
	private final String stringValue;
	
	private final String representValue;
	
	private ProductStatus(String stringValue, String representValue) {
		this.stringValue = stringValue;
		this.representValue = representValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public String getRepresentValue() {
		return representValue;
	}

}
