package potato.enterprise.distributedprogramming.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import potato.enterprise.distributedprogramming.clientsimulator.Player;
import potato.enterprise.distributedprogramming.util.GameType;
import potato.enterprise.distributedprogramming.util.Settings;

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

    /**
     * Creación de una partida y comunicación a los jugadores.
     *
     * @param type tipo de la partida a crear
     */
    private void checkGame(GameType type) {
        Game game;

        try (OutputStream os = this.socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter writer = new PrintWriter(osw)) {

            // Comprobar el tipo de partida y crear la partida en función de su tipo
            switch (type) {
                case DICE:
                    game = startGame(type, Settings.DICE_QUEUE, Settings.DICE_SEMAPHORE, 1);
                    // Enviar los datos de los jugadores a cada uno de la partida
                    /*
                    Para el caso de una partida de dados, sabemos que solo habrá dos
                    jugadores, y el primero de la lista será el anfitrión
                     */
                    for (Player p : game.getPlayerList()) {
                        // Si no es el mismo jugador que está siendo atendido por la petición
                        if (!p.equals(this.player)) {
                            // Enviar datos del jugador
                            sendPlayerData(writer, p);
                        }
                    }

                    // Informa al jugador si es o no el anfitrión de su partida
                    writer.println((this.player.isHost()) ? "Eres anfitrión" : "Eres Invitado");
                    writer.flush();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enviar los datos de cada jugador de la partida al resto de jugadores
     *
     * @param writer PrintWriter en el que escribir los datos
     * @param player jugador del que sacar los datos a enviar
     */
    private void sendPlayerData(PrintWriter writer, Player player) {
        writer.println(String.format("Nickname: %s Dirección: %s Puerto: %d", 
                player.getNickname(), player.getHostDir(), player.getPort()));
        writer.flush();
    }

    private Game startGame(GameType type, Queue<Player> queue, Semaphore semaphore, int permits) {

    }
}
