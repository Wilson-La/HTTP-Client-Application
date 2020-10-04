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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class httpc {
	
	private static String path;
	private static String hostName;
	private static Header headers[];
	private static String generalHelp;
	private static String getHelp;
	private static String postHelp;
	private static Socket clientSocket = new Socket();
	private static String port = "80";
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
			for(int i = 0; i < headerArr.size(); i++){
				msg += headerArr.get(i);
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
