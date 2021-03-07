package potato.enterprise.distributedprogramming.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mcarr
 */
public class ServerMain {

    /**
     * Manejador de peticiones por parte del servidor.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket socket = null;

        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            server.bind(addr);

            while (true) {
                socket = server.accept();
                System.out.println("Jugador conectado al servidor");
                Petition petition = new Petition(socket);
                petition.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
