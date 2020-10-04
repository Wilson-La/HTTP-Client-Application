import java.net.InetSocketAddress;
import java.net.Socket;
//import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

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

		//help method
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
	 
	 //static variables
	 //verbose
	 private static boolean v = false;
	 //header
	 private static boolean h = false;
	 //in line data
	 private static boolean hasInline = false;
	 //reading from file
	 private static boolean readFile = false;
	 //header data
	    
	    
	    
	 //Information to provide
	 private static String headerData = "";
	 private static String inLineData = "";
	 private static String path = "";
	 private static String hostName = "";
	 private static String outDirectory = "";
	 private static String arguments = "";
	 private static String msgBuilder = "";
	 private static String port = "80";
	 private static String[] hostArgs = new String[2];
	 private static Socket socket = new Socket();

	 
	 // url parser
	  public static void urlParser(String url) {
	        if(url.contains("../")){
	            hostArgs = url.split("/", 2);
	            outDirectory = hostArgs[0];
	            arguments = hostArgs[1];
	        }else if (url.contains("//")){
	            hostArgs = url.split("//");
	            if (url.contains("/")){
	                hostArgs = hostArgs[1].split("/");
	                hostName = hostArgs[0];
	                arguments = hostArgs[1];
	            }
	        }else if (url.contains("/")){
	            hostArgs = url.split("/", 2);
	            hostName = hostArgs[0];
	            arguments = hostArgs[1];
	        }
	        
	        else{
	            hostName=url;
	        }

	        if (hostName.contains("localhost")){
	            hostArgs = hostName.split(":", 2);
	            hostName = hostArgs[0];
	            port = hostArgs[1];
	        }
	    }

	    /**
	     * This method takes the data provided after the -d option and parses it.
	     * @param inLineData is the data from the cmd after -d
	     * @return a string that contains the same data but formatted as UTF-8 format
	     */
	    public static String inLineDataParser(String inLineData) {
	        //replaces all whitespace and non-visible character from the inline data
	        inLineData = inLineData.replaceAll("\\s", "");
	        String param = "";
	        if (inLineData.charAt(0)=='{'){
	            inLineData = inLineData.substring(1, inLineData.length()-1);
	        }
	        String[] args_arrayStrings = inLineData.split("&|,|\n");
	        try{
	            for (String s: args_arrayStrings){
	                String[] each_args_arrayStrings = s.split("=|:");
	                for (String s1: each_args_arrayStrings){
	                    if (s1.charAt(0)=='"'){
	                        s1 = s1.substring(1, s1.length()-1);
	                    }
	                    param = param.concat(URLEncoder.encode(s1, "UTF-8"));
	                    param = param.concat("=");
	                }
	                param = param.substring(0, param.length()-1);
	                param = param.concat("&");
	            }
	        }catch (Exception e){
	            System.out.println("Exception in inLineDataParser.\n"+e.getMessage());
	        }
	        return param.substring(0, param.length() - 1);
	        }   
	    
	    public static String readingFromFile(String path) {
	        String line_ = "";
	        try{
	            File file = new File(path);
	            BufferedReader input_file = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	            String line;
	            while((line = input_file.readLine()) != null) {
	                line_ = line_.concat(line);
	            }
	            input_file.close();
	        }catch(Exception e){
	            System.out.println("Exception in readingFromFile!!!"+e.getMessage());
	        }
	        return line_;
	    }

	    public static String createMessage(String requestType, String arguments, boolean hasHeader, boolean hasData) {
	        String message = "";
	        final String HTTP = (" HTTP/1.0\r\n");
	        if (requestType=="GET /") {
	            message = "GET "+outDirectory+"/"+arguments+HTTP+"\r\n";
	            if (hasHeader){
	                message = message.replace("\r\n\r\n", ("\r\n"+headerData+"\r\n"));
	            }
	        } else {
	            message = requestType+arguments+HTTP;
	            message = message.concat("Content-Length: "+inLineData.length()+"\r\n");
	            message = message.concat(headerData+"\r\n");
	            if(hasData){
	                message = message.concat(inLineData+"\r\n");
	            }
	        }
	        message = message.concat("\r\n");
	        System.out.println(message);
	        return message;
	    }
	    
	    /**
	     * This is a common method that can be called for both get and post requests.
	     */
	    public static void sendMessage(String messageBuilder) {
	        try {
	            socket.connect(new InetSocketAddress(hostName, Integer.parseInt(port)));
	            BufferedWriter socketBufferedWriterOutputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	            BufferedReader socketBufferedReaderInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            socketBufferedWriterOutputStream.write(msgBuilder);
	            socketBufferedWriterOutputStream.flush();
	            String response = " ";

	            while ((response = socketBufferedReaderInputStream.readLine()) != null) {
	                if ((response.length()==0) && !v){
	                    StringBuilder res_recvd = new StringBuilder();
	                    while ((response = socketBufferedReaderInputStream.readLine()) != null){
	                        res_recvd.append(response).append("\r\n");
	                    }
	                    System.out.println(res_recvd.toString());
	                    v = false;
	                    break;
	                }else if (v){
	                    System.out.println(response);
	                }
	            }
	            socketBufferedWriterOutputStream.close();
	            socketBufferedReaderInputStream.close();
	            socket.close();
	        } catch (Exception e) {
	            System.out.println("ERROR from the sendMessage method.\n"+e.getMessage());
	        }
	    }
	//get method
	public static void get(String input) {
      urlParser(input);  

      msgBuilder = createMessage("GET /", arguments, h, false);
      
      sendMessage(msgBuilder);    
		
	}
	 
	//post method
		public static void post(String input) {
	        urlParser(input);
	        
	        if (hasInline && readFile){
	            System.out.println(" Cannot combine -d and -f.");
	            System.exit(1);
	        }
	        else if (readFile){
	            hasInline = true;
	            inLineData = readingFromFile(path);
	            inLineData = inLineDataParser(inLineData);
	        }
	        else if (hasInline){
	            inLineData = inLineDataParser(inLineData);
	        }

	        msgBuilder = createMessage("POST /", arguments, h, hasInline);

	        sendMessage(msgBuilder);
		}
			
	 
	    public static void cmdInput(String[] args){
	        for (int i =0; i<args.length; i++){
	            if (args[i].equalsIgnoreCase("-v")){
	            	v = true;
	            }else if (args[i].equalsIgnoreCase("-h")){
	                h = true;
	                headerData = headerData.concat(args[i+1]+"\r\n");
	                i++;
	            }else if (args[i].equalsIgnoreCase("-d")){
	                hasInline = true;
	                inLineData = (args[i+1]);
	                i++;
	            }else if (args[i].equalsIgnoreCase("-f")){
	                readFile = true;
	                path = (args[i+1]);
	                i++;
	            }else if (args[i].equalsIgnoreCase("get")){
	                getRequest = true;
	            }else if (args[i].equalsIgnoreCase("post")){
	                postRequest = true;
	            }else if (args[i].equalsIgnoreCase("help")){
	                helpMsg = true;
	            }else{
	                url = (args[i]);
	            }
	       }
	    }
	}





