public class httpc {
	
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
}
