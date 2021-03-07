package potato.enterprise.distributedprogramming.clientsimulator;

/**
 *
 * @author mcarr
 */
public class PlayerLauncher {

    /**
     * Generador de 10 jugadores con sus identificadores.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Player player = new Player(i);
            player.start();
        }
    }

}
