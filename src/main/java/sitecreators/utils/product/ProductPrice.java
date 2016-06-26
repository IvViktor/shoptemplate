/**
 * 
 */
package sitecreators.utils.product;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;

import sitecreators.utils.finance.Currency;

/**
 * @author User
 *
 */

@Embeddable
public class ProductPrice {
	
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "currency_id",foreignKey = @ForeignKey(name = "CURRENCY_ID_FK"))
	private Currency currency;
	
	

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
