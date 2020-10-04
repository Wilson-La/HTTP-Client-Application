
public class httpc {
	
	private static String path;
	private static String hostName;
	private static Header headers[];
	
	public static void main (String[] args){
		
		
		
	}
	// url parser method to seperate the url into host name and path
	public static void urlParser(String url){
		if(url.contains("http://")){
			url = url.substring(7);
			if(url.contains("/")){
				int slashIndex = url.indexOf("/");
				hostName = url.substring(0, slashIndex);
				path = url.substring(slashIndex);
			}
			else{
				hostName = url;
				path = "/";
			}
		}
		else{
			if(url.contains("/")){
				int slashIndex = url.indexOf("/");
				hostName = url.substring(0, slashIndex);
				path = url.substring(slashIndex);
			}
			else{
				hostName = url;
				path = "/";
			}
		}
	}
	
	public static String createMsg(String method, String path){
		String msg = "";
		Header headerArr[] = new Header[2];
		Header header1 = new Header("Host", hostName);
		Header header2 = new Header("Accept", "*/*");
		Header header3 = new Header("User-agent", "Concordia-HTTP/1.0");
				
		headerArr[0] = header1;
		headerArr[1] = header2;
		headerArr[2] = header3;
		if (method.equals("GET")){
			msg = "GET /"+path+"/ HTTP 1.0\r\n"+"\r\n";
		}
		else {
			
		}
		return msg;
	}
	
	//GET method
	public static void get(String urlInput){
		
		
		
	}
	//POST method
	public static void post(){
		
	}
}
