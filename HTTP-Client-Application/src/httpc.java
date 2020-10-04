import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class httpc {
	
	private static String path;
	private static String hostName;
	private static Header headers[];
	private static String generalHelp;
	private static String getHelp;
	private static String postHelp;
	private static Socket clientSocket = new Socket();
	private static String port = "8080";
	private static boolean verbose = false;
	private static String messageToSend;
	
	
	public static void main(String[] args) {
		
		
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
	//createmsg() method used to create the request for either a GET or POST
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
	//The sendMsg() method used to send request to server and also to receive the response following our request
	public static void sendMsg(String msg){
		try {
			clientSocket.connect(new InetSocketAddress(hostName, Integer.parseInt(port)));
			
			BufferedWriter socketBWOS = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			
			BufferedReader socketBRIS = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			//socketBWOS write's the created message
			socketBWOS.write(msg);
			socketBWOS.flush();
			String response = "";
			//the following loop is used to recieve the response
			while ((response = socketBRIS.readLine()) != null){
				if(response.length() == 0 && !verbose){
					StringBuilder recieved_response = new StringBuilder();
					while ((response = socketBRIS.readLine()) != null){
						recieved_response.append(response).append("\r\n");
					}
					System.out.println(recieved_response.toString());
					break;
				}
				else if(verbose){
					System.out.println(response);
				}
			}
			socketBWOS.close();
			socketBRIS.close();
			clientSocket.close();
		}catch (Exception e) {
			System.out.println("sendMsg() method has encountered an error.\n"+e.getMessage());
		}
	}
	
	/*
	GET method which uses the urlParser() to parse the url into hostName and path.
	The method then uses the createMsg() method to create the GET request to be sent
	Then the method finally uses the sendMsg() method to send the GET request
	*/
	public static void get(String urlInput){
		
		urlParser(urlInput);
		
		messageToSend = createMsg("GET", path);
		
		sendMsg(messageToSend);
	}
	//POST method
	public static void post(String urlInput){
		
	}
}
