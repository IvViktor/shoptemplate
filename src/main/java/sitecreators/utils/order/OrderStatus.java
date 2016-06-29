package sitecreators.utils.order;

public enum OrderStatus {
	
	ACTIVE("ACTIVE"),
	FULLFILLED("FULLFILLED"),
	DEFFERED("DEFFERED"),
	CANCELED("CANCELED"),
	INCART("INCART");
	
	private final String representValue;
	
	private OrderStatus(String str) {
		this.representValue = str;
	}

	public String getRepresentValue() {
		return representValue;
	}

		
}
