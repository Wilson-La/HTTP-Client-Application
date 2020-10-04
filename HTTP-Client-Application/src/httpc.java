import java.net.InetSocketAddress;
import java.net.Socket;

public class httpc {
	
	private static String path;
	private static String hostName;
	private static Header headers[];
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
			//argument condition
		 	if ( args.length == 0){
	            System.out.println("For More information use commands:\n\t\n httpc help \t\n httpc get help \t\n httpc post help\n");
	        }
		 	else{
	            Client.cmdInput(args);
	        }
		 	
		 	//help condition
		 	
		 	//help request
	        if (Client.getHelpMsg()) {
	            Client.help();
	        }
	        //get request
	        else if(Client.getGet()){
	            Client.get(Client.getUrl());
	        }
	        //post request
	        else if (Client.getPost()){
	            Client.post(Client.getUrl());
	        }		
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
		Header headerArr[] = new Header[3];
		Header header1 = new Header("Host", hostName);
		Header header2 = new Header("Accept", "*/*");
		Header header3 = new Header("User-agent", "Concordia-HTTP/1.0");
				
		headerArr[0] = header1;
		headerArr[1] = header2;
		headerArr[2] = header3;
		if (method.equals("GET")){
			msg = "GET /"+path+"/ HTTP 1.0\r\n";
			for(int i = 0; i < headerArr.length; i++){
				msg += headerArr[i].toString();
			}
			msg += "\r\n";
		}
		else {
			Header header4 = new Header("Content-length","255");
			headerArr[1] = header4;
			msg = "POST /"+path+"/ HTTP 1.0\r\n";
			for(int i = 0; i < headerArr.length; i++){
				msg += headerArr[i].toString();
			}
			msg += "\r\n";
		}
		return msg;
	}
	//The sendMsg() method
	public static void sendMsg(){
		
	}
	
	//GET method
	public static void get(String urlInput){
		
		
		
	}
	//POST method
	public static void post(String urlInput){
		
	}
}
