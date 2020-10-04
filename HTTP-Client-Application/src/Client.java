/* COMP-445
 * Lab Assignment #1
 * Student 1:
 * Name: Achoura Bague
 * Student ID: 27877986
 * Student 2:
 * Name: Wilson La
 * Student ID: 27738986
 * */

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client {


	 	private static boolean getRequest;
	 	private static boolean postRequest;
	 	private static boolean helpMsg=false;
	 	private static String url;
 	
	 	//constructors
		public Client () {
		    Client.getRequest = false;
			Client.postRequest = false;
			Client.helpMsg= false;
			Client.url="";
			
		}
		//constructor with parameters
		public Client (Boolean getRequest,Boolean postRequest, Boolean helpMsg,String url) {
			Client.getRequest = getRequest;
			Client.postRequest = postRequest;
			Client.helpMsg = helpMsg;
			Client.url = url;
				
		} 
		// Accessors and mutators
		public static Boolean getGet() {
			return Client.getRequest;
		}
		public static Boolean getPost() {
			return Client.postRequest;
		}
		public static Boolean getHelpMsg() {
			return Client.helpMsg;
		}
		public static String getUrl() {
			return Client.url;
		}
		public static void setGet(Boolean getRequest) {
			Client.getRequest = getRequest ;
		}
		public static void setPost(Boolean postRequest) {
			Client.postRequest = postRequest;
		}
		public static void setHelpMsg(Boolean helpMsg) {
			Client.helpMsg= helpMsg;
		}
		public static void setUrl(String url) {
			Client.url=url;
		}
		
		
	 	//Variables
		private static String path;
		private static String hostName;
		private static Header headers[];
		private static String generalHelp;
		private static String getHelp;
		private static String postHelp;
		private static Socket clientSocket = new Socket();
		private static String port = "80";
		private static String messageToSend;
		
	   	private static String dataHeader = "";
	    	private static boolean h = false;
	    	private static String dataInline = "";
	    	private static boolean hasInline = false;
	    	private static boolean readFile = false;
	 	private static boolean v = false;
		
	
	 	//methods

	 
	 	//help 
	 
	 	public static void help() {
		
	 		String generalHelp,getHelp,postHelp;
		 
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
		
		//The readFromFile() method gets a filePath to open a file to read from it and returns the text from the file as a string
		public static String readFromFile(String filePath){
			String fullyReadInput = "";
			try{
				FileReader fileToReadFrom = new FileReader(filePath);
				BufferedReader brInput = new BufferedReader(fileToReadFrom);
				String currLine;
				while((currLine = brInput.readLine()) != null){
					fullyReadInput = fullyReadInput.concat(currLine);
				}
				brInput.close();
			}
			catch(Exception e){
				System.out.println("readFromFile() method has encountered an error. \n"+e.getMessage());
			}
			return fullyReadInput;
		}
	 
		//createmsg() method used to create the request for either a GET or POST
		public static String createMsg(String method, String path){
			String msg = "";
			ArrayList<Header> headerArr = new ArrayList<Header>();
			if (method.equals("GET")){
				Header header1 = new Header("Host", hostName);
				Header header2 = new Header("Accept", "*/*");
				Header header3 = new Header("User-agent", "Concordia-HTTP/1.0");
						
				headerArr.add(header1);
				headerArr.add(header2);
				headerArr.add(header3);
				msg = "GET /"+path+"/ HTTP 1.0\r\n";
				for(int i = 0; i < headerArr.size(); i++){
					msg += headerArr.get(i);
				}
				msg += "\r\n";
			}
			else {
				
				msg = "POST /"+path+"/ HTTP 1.0\r\n";
				if(h){
					String cmdHeaderArr[] = dataHeader.split("\\r?\\n");
					for(int i = 0; i < cmdHeaderArr.length; i++){
						String splitByKeyVal[] = cmdHeaderArr[i].split(":");
						Header header4 = new Header(splitByKeyVal[0], splitByKeyVal[1]);
						headerArr.add(header4);
					}
					h = false;
				}
				for(int i = 0; i < headerArr.size(); i++){
					msg += headerArr.get(i);
				}
				if(hasInline){
					msg += dataInline;
					hasInline = false;
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
					if(response.length() == 0 && !v){
						StringBuilder recieved_response = new StringBuilder();
						while ((response = socketBRIS.readLine()) != null){
							recieved_response.append(response).append("\r\n");
						}
						System.out.println(recieved_response.toString());
						break;
					}
					else if(v){
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
	 
		//command line
		public static void cmdInput(String[] args){
			for (int i =0; i<=args.length-1; i++){
    	
				//help command
				if (args[i].equals("help")){
					helpMsg = true;
				}
				//get command	
				else if (args[i].equals("get")){
					getRequest = true;
				}
				//post command
				else if(args[i].equals("post")){
					postRequest = true;
				}
				//verbose command
				else if(args[i].equals("-v")) {
					v=true;
				}
				//headers
				else if(args[i].equals("-h")) {
					h=true;
					dataHeader = dataHeader.concat(args[i+1]+"\r\n");
					i++;
				}
				//inline data
				else if(args[i].equals("-d")){
					hasInline=true;
					dataInline = (args[i+1]);
					i++;
				}
				//readfile
				else if(args[i].equals("-f")){
					readFile = true;
					path = (args[i+1]);
					i++;
				}
				//url
				else
					url = (args[i]);
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
	
			urlParser(urlInput);
	
			//Incomplete condition to be added
	
			messageToSend = createMsg("POST", path);
	
			sendMsg(messageToSend);
	
		}
	 
}	





