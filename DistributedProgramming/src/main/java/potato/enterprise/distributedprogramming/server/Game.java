package potato.enterprise.distributedprogramming.server;

import java.util.ArrayList;
import java.util.List;
import potato.enterprise.distributedprogramming.clientsimulator.Player;
import potato.enterprise.distributedprogramming.util.GameType;

/**
 *
 * @author mcarr
 */
public class Game {

    private Integer id;
    List<Player> playerList;
    GameType type;
    Player gameHost;

    public Game(Integer id, GameType type) {
        this.id = id;
        this.type = type;
        this.playerList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void addToPlayerList(Player player) {
        this.playerList.add(player);
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public Player getGameHost() {
        return playerList.get(0);
    }

    public void setGameHost(Player gameHost) {
        this.gameHost = gameHost;
    }

    public void assignGameToPlayer() {
        for (Player player : playerList) {
            player.setCurrentGame(this);
        }
    }
}
