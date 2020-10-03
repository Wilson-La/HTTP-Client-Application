
public class httpc {
	
	private static String path;
	private static String hostName;
	
	public static void main (String[] args){
		
	}
	
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
	
	//GET method
	public static void get(String urlInput){
		
		
		
	}
	//POST method
	public static void post(){
		
	}
}
