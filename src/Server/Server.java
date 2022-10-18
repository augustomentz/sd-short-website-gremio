package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

import Page.Page;

public class Server {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    ServerSocket server;
    
    List<String> screensPaths = List.of(
    		"/index.html", 
    		"/contato.html", 
    		"/galeria.html"
    );
    List<String> imagesPaths = List.of(
    		"/images/logo.png",
    		"/images/background.jpg", 
    		"/images/photo_1.jpg", 
    		"/images/photo_2.jpg",
    		"/images/photo_3.jpg",
    		"/images/photo_4.jpg"
    );
    
    public void config(Integer port){
        try {
        	server = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void listenConnections(){
        try {            
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void receiveRequest() {
        String line = "";

        try {
            while((line = in.readLine()).length() > 0){
            	String path = line.split(" ")[1];
            	
            	if (screensPaths.contains(path)) {
            		sendReplyHtml(new Page().getPageHtml("src/html/" + path));
            	}

            	if (imagesPaths.contains(path)) {
            		sendReplyImage("src" + path, path.contains("png") ? "png" : "jpeg");
            	}
            }
            
        	System.out.println("\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void sendReplyHtml(String content){	
    	try {
    		String response = "HTTP/1.1 200 OK\n" +
    	            "Content-Type: text/html;\n" +
    	            "Server: Sist.Dist. Server 1.0\n" +
    	            "Connection: close\n" +
    	            "Content-Length: " + content.getBytes().length + "\n" +
    	            "\n" +
    	            content;
    		
            out.println(response);
            out.flush();
    	} catch (Exception e) {
    		System.out.println(e);
    	}

    }
    
    public void sendReplyImage(String path, String format) {
		try {
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		
			File file = new File(path);
			FileInputStream inFile  = new FileInputStream(path);
			byte[] fileContent = Files.readAllBytes(file.toPath());
	        inFile.read(fileContent);
	        
			String response = "HTTP/1.1 200 OK\n" +
	            "Content-Type: image/" + format + "\n" +
	            "Server: Sist.Dist. Server 1.0\n" +
	            "Connection: close\n" +
	            "Content-Length: " + file.length() + "\n" +
	            "\n";
			
			outToClient.writeBytes(response);
			outToClient.write(fileContent, 0, (int) file.length());  
			outToClient.flush();
			inFile.close();
		} catch (IOException e) {
			System.out.println(e);
		}	
    }
}
