public class Header {
	
	//the attributes
	private String key;
	private String value;
	
	
	//constructors
	public Header () {
	    this.key = null;
		this.value = null;
		
	}
	
	public Header (String theKey, String theValue) {
		theKey = key;
		theValue = value;
			
	} 
	// Accessors and mutators
	public String getKey() {
		return this.key;
	}
	public String getValue() {
		return this.value;
	}
	public void setKey(String theKey) {
		theKey = key;
	}
	public void setValue(String theValue) {
		theValue = value;
	}

	public Boolean equals(Header hdr) {
		return (key == hdr.key);
	}
	public String toString() {
		return (key + ": " + value + "\r\n");
	}
	
}