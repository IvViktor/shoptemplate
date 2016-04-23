/**
 * 
 */
package sitecreators.utils.finance;

import javax.persistence.Entity;

/**
 * @author User
 *
 */
@Entity
public class Currency {
	
	private String title;
	
	private double koef;
	
	private String symbol;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getKoef() {
		return koef;
	}

	public void setKoef(double koef) {
		this.koef = koef;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	

}
