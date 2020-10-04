
public class httpc {
	
	private static String path;
	private static String hostName;
	private static String generalHelp;
	private static String getHelp;
	private static String postHelp;
	
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
