package potato.enterprise.distributedprogramming.clientsimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import potato.enterprise.distributedprogramming.server.Game;
import potato.enterprise.distributedprogramming.util.Settings;

/**
 *
 * @author rosty
 */
public class Player extends Thread{
    
    private Integer identifier;
    private int port;
    private String nickname;
    private String hostDir;
    private boolean host;
    private Game currentGame;

    public Player(Integer identifier) {
        this.identifier = identifier;
        this.nickname = "Jugador".concat(identifier.toString());
        this.hostDir = "localhost";
        this.port = Settings.increasePortNumber();
        this.host = false;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHostDir() {
        return hostDir;
    }

    public void setHostDir(String hostDir) {
        this.hostDir = hostDir;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void run() {
        String[] conection;
        conection = connectionToServer("localhost", 5555, new String[]{"initGame", "dice",
            getNickname(), identifier.toString()});

        gameLoop(conection[0], Integer.parseInt(conection[1]));

        if (host) {
            connectionToServer("localhost", 5555, new String[]{"finishGame", getCurrentGame().getId().toString()});
        }
    }

    private String[] connectionToServer(String dir, int port, String[] array) {
        String[] arrayReceive = null;
        try ( Socket socket = new Socket();) {

            System.out.println("Estableciendo la conexión");
            InetSocketAddress addr = new InetSocketAddress(dir, port);
            socket.connect(addr);
            if (array[0].equals("initGame")) {
                Settings.addPlayerToMap(this);
            }
            sendMessage(socket, array);
            arrayReceive = receiveData(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayReceive;
    }

    private void sendMessage(Socket socket, String[] args) {
        try ( OutputStream os = socket.getOutputStream(); // Flujos que manejan caracteres
                  OutputStreamWriter osw = new OutputStreamWriter(os); // Flujos de líneas
                  PrintWriter pWriter = new PrintWriter(osw);) {
            System.out.println("Enviando mensaje");
            for (String string : args) {
                pWriter.println(string);
            }
            pWriter.flush();
            System.out.println("Mensaje enviado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] receiveData(Socket socket) {
        List<String> data = new ArrayList<>();
        String linea;
        try ( InputStream is = socket.getInputStream();  
                InputStreamReader isr = new InputStreamReader(is);  
                BufferedReader bReader = new BufferedReader(isr);) {
            while ((linea = bReader.readLine()) != null) {
                data.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String[]) data.toArray();
    }
    
    private void gameLoop(String string, int parseInt) {
        //No toca
    }
}
