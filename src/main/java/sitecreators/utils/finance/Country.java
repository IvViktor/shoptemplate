package sitecreators.utils.finance;

public enum Country {
	UAH("UAH",'\u20b4',false),
	USD("USD",'$',true),
	EUR("EUR",'\u20ac',false),
	RUB("RUB",'\u20bd',false),
	GBP("GBP",'\u20a4',true);
	
	private final String stringValue;
	
	private final char cc;
	
	private final boolean positionLeft;
	
	private Country(String stringValue, char cc,boolean positionLeft) {
		this.stringValue = stringValue;
		this.cc = cc;
		this.positionLeft = positionLeft;
	}

	public String getStringValue() {
		return stringValue;
	}

	public char getCc() {
		return cc;
	}

	public boolean isPositionLeft() {
		return positionLeft;
	}
	
	
}
