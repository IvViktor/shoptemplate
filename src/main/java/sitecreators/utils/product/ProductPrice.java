/**
 * 
 */
package sitecreators.utils.product;

import sitecreators.utils.finance.Currency;

/**
 * @author User
 *
 */

public class ProductPrice {
	
	private int amount;
	
	private Currency currency;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
