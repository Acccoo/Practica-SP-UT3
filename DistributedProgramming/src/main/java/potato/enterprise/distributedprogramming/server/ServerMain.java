package potato.enterprise.distributedprogramming.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcarr
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket socket = null;
        
        try(ServerSocket server = new ServerSocket()) {
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            server.bind(addr);
            
            while(true) {
                socket = server.accept();
                System.out.println("Jugador conectado al servidor");
                Petition petition = new Petition(socket);
                petition.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
