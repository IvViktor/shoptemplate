/**
 * 
 */
package sitecreators.utils.finance;

import java.util.List;

/**
 * @author viktor
 *
 */
public interface CurrencyDAO {
	
	public List<Currency> getAllCurrencies();
	
	public void removeCurrency(Currency currency);
	
	public void updateCurrencies(List<Currency> updateList);
	
	public void addCurrency(Currency currency);
	
	public void updateCurrency(Currency currency);

}
