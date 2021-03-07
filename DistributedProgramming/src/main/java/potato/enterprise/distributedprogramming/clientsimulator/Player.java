package potato.enterprise.distributedprogramming.clientsimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import potato.enterprise.distributedprogramming.server.Game;
import potato.enterprise.distributedprogramming.util.GameType;
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

    }

    private String[] connectionToServer(String dir, int port, String[] array) {

    }

    private void sendMessage(Socket socket, String[] args) {

    }

    private String[] receiveData(Socket socket) {

    }

    /**
     * Bucle de juego.
     *
     * @param dir dirección del anfitrión a la que conectar los sockets
     * @param port puerto del anfitrión al que conectar los sockets
     * @return resultado de una partida, el cual se enviará de vuelta al
     * servidor
     */
    private String gameLoop(String dir, int port) {
        GameType type = this.currentGame.getType();
        String result = "";

        // Primero comprobamos si el jugador es anfitrión
        if (isHost()) {
            // En caso afirmativo, el jugador es el servidor de una partida, dependiendo del tipo
            try {
                switch (type) {
                    case DICE:
                        result = diceGameAsHost(dir, port);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // En caso negativo, el jugador se conecta al host
            gameAsGuest(dir, port, type);
        }
        return result;
    }

    /**
     * Partida del invitado. Primero se conecta al anfitrión y después realiza
     * una comprobación del tipo de partida.
     *
     * @param dir dirección a la que se debe conectar el invitado
     * @param port puerto al que se debe conectar el invitado
     * @param type tipo de partida
     * @return resultado de la partida
     */
    private String gameAsGuest(String dir, int port, GameType type) {
        String result = "";

        // Creación del Socket y conexión con el anfitrión
        try (Socket socket = new Socket()) {
            InetSocketAddress addr = new InetSocketAddress(dir, port);
            socket.connect(addr);

            // Jugar a la partida correspondiente
            switch (type) {
                case DICE:
                    result = guestDiceGame(socket);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Comunicación del invitado con el anfitrión para una partida de dados.
     *
     * @param socket socket invitado
     * @return resultado de la partida
     * @throws IOException
     */
    private String guestDiceGame(Socket socket) throws IOException {
        int number;
        String result = "E";

        try (InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter writer = new PrintWriter(osw)) {

            // Mientras haya empate
            while (result.equals("E")) {
                // El invitado es el primero en lanzar el dado
                number = getRandomInt(6);
                writer.println(number);
                writer.flush();
                // Cuando el anfitrión lanza el dado, envía el resultado al invitado
                result = br.readLine();
            }
        }
        return result;
    }

    /**
     * Conexión del invitado y el anfitrión para una partida de dos jugadores,
     * como por ejemplo una de dados.
     *
     * @param dir dirección en la que escuchar
     * @param port puerto en el que escuchar
     * @return resultado de la partida
     * @throws IOException
     */
    private String diceGameAsHost(String dir, int port) throws IOException {
        Socket connected = null;
        String result = "";

        // Creación del ServerSocket
        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress addr = new InetSocketAddress(dir, port);
            server.bind(addr);

            // En el caso de una partida de dados, la primera conexión aceptada
            // será la del socket invitado
            connected = server.accept();
            result = hostDiceGame(connected);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connected != null) {
                connected.close();
            }
        }

        return result;
    }

    /**
     * Comunicaciñon del anfitrión con el invitado para una partida de dados.
     * Genera el resultado de la partida y se lo envía a este último.
     *
     * @param socket socket del invitado
     * @return resultado de la partida
     * @throws IOException
     */
    private String hostDiceGame(Socket socket) throws IOException {
        int hostNumber, guestNumber;
        String result = "E";

        try (InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                Scanner sc = new Scanner(isr);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PrintWriter writer = new PrintWriter(osw)) {

            // Mientras el resultado sea empate
            while (result.equals("E")) {
                // El anfitrión recibe la tirada del invitado
                guestNumber = sc.nextInt();
                sc.nextLine();
                // Tira su dado
                hostNumber = getRandomInt(6);

                // Comparar resultados
                result = compareDiceResults(guestNumber, hostNumber);

                // Enviar resultado al invitado
                writer.println(result);
                writer.flush();
            }
        }
        return result;
    }

    /**
     * Comparar el resultado obtenido por los dos jugadores de una partida de
     * dados.
     *
     * @param guestNumber número del invitado
     * @param hostNumber número del anfitrión
     * @return resultado desde el punto de vista del anfitrión
     */
    private String compareDiceResults(int guestNumber, int hostNumber) {
        String result;

        if (hostNumber > guestNumber) {
            result = "V";
        } else if (guestNumber > hostNumber) {
            result = "D";
        } else {
            result = "E";
        }

        return result;
    }

    private int getPlayersAmount(GameType type) {
        int value = 0;

        switch (type) {
            case DICE:
            case CHEST:
                value = 2;
                break;
            case PARCHESI:
                value = 4;
                break;
            default:
                value = 0;
        }

        return value;
    }

    /**
     * Obtener número aleatorio entre 1 y max.
     *
     * @param max máximo valor del número aleatorio
     * @return número aletatorio generado
     */
    private int getRandomInt(int max) {
        return (int) Math.floor(Math.random() * Math.floor(max)) + 1;
    }
}
