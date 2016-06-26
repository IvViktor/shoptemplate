package sitecreators.utils.finance;

public enum Country {
	UAH("UAH",'\u20b4'),
	USD("USD",'$'),
	EUR("EUR",'\u20ac'),
	RUB("RUB",'\u20bd'),
	GBP("GBP",'\u20a4');
	
	private final String stringValue;
	
	private final char cc;
	
	private Country(String stringValue, char cc) {
		this.stringValue = stringValue;
		this.cc = cc;
	}

	public String getStringValue() {
		return stringValue;
	}

	public char getCc() {
		return cc;
	}
	
	
}
