/**
 * 
 */
package sitecreators.utils.finance;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author User
 *
 */
@Entity
public class Currency {
	
	@Enumerated(EnumType.STRING)
	private Country countryCode;
	
	private double koef;
	

}
