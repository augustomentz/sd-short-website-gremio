import Server.Server;

public class Api {
    public static void main(String[] args) {
        Server server = new Server();
        
        server.config(1903);
       
        while(true) {
        	server.listenConnections();
            server.receiveRequest();	
        }
    }
}
