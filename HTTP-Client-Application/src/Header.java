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
		key = theKey;
		value = theValue;
			
	} 
	// Accessors and mutators
	public String getKey() {
		return this.key;
	}
	public String getValue() {
		return this.value;
	}
	public void setKey(String theKey) {
		key = theKey;
	}
	public void setValue(String theValue) {
		value = theValue;
	}

	public Boolean equals(Header hdr) {
		return (key == hdr.key);
	}
	public String toString() {
		return (key + ": " + value + "\r\n");
	}
	
}
