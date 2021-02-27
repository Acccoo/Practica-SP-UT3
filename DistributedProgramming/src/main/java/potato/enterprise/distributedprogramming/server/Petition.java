package potato.enterprise.distributedprogramming.server;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import potato.enterprise.distributedprogramming.clientsimulator.Player;
import potato.enterprise.distributedprogramming.util.GameType;

/**
 *
 * @author mcarr
 */
public class Petition extends Thread {

    private Socket socket;
    private Player player;

    public Petition(Socket socket) {
        this.socket = socket;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        
    }
    
    private void checkGame(GameType type) {
        
    }
    
    private Game startGame(GameType type, Queue<Player> queue, Semaphore semaphore, int permits) {
        
    }
}
