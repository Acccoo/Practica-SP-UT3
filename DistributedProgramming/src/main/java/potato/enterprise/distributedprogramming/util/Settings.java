package potato.enterprise.distributedprogramming.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import potato.enterprise.distributedprogramming.clientsimulator.Player;
import potato.enterprise.distributedprogramming.server.Game;

/**
 *
 * @author mcarr
 */
public class Settings {

    public static Map<Integer, Game> ACTUAL_GAMES = new HashMap();      // Mapa con las partidas en curso
    public static Map<Integer, Player> CONNECTED_PLAYERS = new HashMap();    // Mapa con los jugadores conectados al servidor 
    public static Queue<Player> DICE_QUEUE = new LinkedList<>();     // Cola de jugadores a la espera de una partida de dados
    public static Queue<Player> PARCHESI_QUEUE = new LinkedList<>();     // Cola de jugadores a la espera de una partida de parchís
    public static Queue<Player> CHEST_QUEUE = new LinkedList<>();        // Cola de jugadores a la espera de una partida de ajedrez
    public static Semaphore MUTEX = new Semaphore(1);   // Controlar la retirada de jugadores de las colas de partida
    public static Semaphore DICE_SEMAPHORE = new Semaphore(0);
    public static Semaphore PARCHESI_SEMAPHORE = new Semaphore(0);
    public static Semaphore CHEST_SEMAPHORE = new Semaphore(0);
    public static int ID = 0;       // Identificadores de las partidas que se vayan creando
    public static int PORTS = 5568;     // Número de puerto sin repetición para cada jugador

    public static void addGameToMap(Game game) {
        ACTUAL_GAMES.put(game.getId(), game);
    }

    public static void removeGameFromMap(Integer id) {
        ACTUAL_GAMES.remove(id);
    }

    public static void addPlayerToMap(Player player) {
        CONNECTED_PLAYERS.put(player.getIdentifier(), player);
    }

    public static Player removePlayerFromMap(Integer id) {
        return CONNECTED_PLAYERS.remove(id);
    }

    public static void addPlayerToQueue(Queue<Player> queue, Player player) {
        queue.add(player);
    }

    public static Player removePlayerFromQueue(Queue<Player> queue) {
        return queue.poll();
    }

    public static int getQueueSize(Queue<Player> queue) {
        return queue.size();
    }

    public static synchronized int increaseGameId() {
        return ID++;
    }

    public static synchronized int increasePortNumber() {
        return PORTS++;
    }
}
