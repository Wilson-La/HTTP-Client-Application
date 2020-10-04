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
	
	//help method
	public static void help() {
		
					//general help message
					generalHelp= "\nhttpc help\n\n"
							+"httpc is a curl-like application but supports HTTP protocol only.\n\n"
							+ "Usage:\n\n"
							+ "\thttpc command [arguments]\n\n"
							+ "The commands are:\n\n"
							+ "\tget     executes a HTTP GET request and prints the response.\n"
							+ "\tpost    executes a HTTP POST request and prints the response.\n"
							+ "\thelp    prints this screen.\n\n\n"
							+ "Use \"httpc help [command]\" for more information about a command.\n\n";
		
					// get help message
					getHelp= "\nhttpc get help\n\n"
							+"usage: httpc get [-v] [-h key:value] URL\n\n"
							+ "Get executes a HTTP GET request for a given URL.\n\n"
							+ "-v Prints the detail of the response such as protocol, status, and headers.\n"
							+"-h key:value Associates headers to HTTP Request with the format 'key:value'.\n\n";
	
					// post help message
					postHelp= "\nhttpc post help\n\n"
							+"usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n\n"
							+ "Post executes a HTTP POST request for a given URL with inline data or from file.\n\n"
							+ "-v            Prints the detail of the response such as protocol, status, and headers.\n"
							+ "-h key:value  Associates headers to HTTP Request with the format 'key:value'.\n"
							+ "-d string     Associates an inline data to the body HTTP POST request.\n"
							+ "-f file       Associates the content of a file to the body HTTP POST request.\n\n"
							+ "Either [-d] or [-f] can be used but not both.";
					
					// get help versus post help versus general help condition
					 if (getRequest){
				            System.out.println(getHelp);
				            System.exit(0);
				        }
					 else if (postRequest){
				            System.out.println(postHelp);
				            System.exit(0);
				        }
					 else{
				            System.out.println(generalHelp);
				            System.exit(0);
				        }
	}
}
