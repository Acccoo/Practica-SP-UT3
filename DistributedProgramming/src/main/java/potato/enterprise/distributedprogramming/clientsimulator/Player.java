package potato.enterprise.distributedprogramming.clientsimulator;

import java.net.Socket;
import potato.enterprise.distributedprogramming.server.Game;
import potato.enterprise.distributedprogramming.util.Settings;

/**
 *
 * @author mcarr
 */
public class Player extends Thread {
    
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
        
    }
    
    private String[] connectionToServer(String dir, int port, String[] array) {
        
    }
    
    private void sendMessage(Socket socket, String[] args) {
        
    }
    
    private String[] receiveData(Socket socket) {
        
    }
    
    private void gameLoop(String dir, int port) {
        
    }
}
