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

    /**
     *  Constructor del player, metiendole los valores requeridos para el juego
     * @param identifier se le pasa por parametros el identificador del jugador
     */
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

    /**
     * El run del juego donde hara las conexiones que tiene segun el tipo de jugador
     */
    @Override
    public void run() {
        String[] conection;
        conection = connectionToServer("localhost", 5555, new String[]{"initGame", "dice",
            getNickname(), identifier.toString()});

        gameLoop(conection[0], Integer.parseInt(conection[1]));

        //si el jugador  es anfitrion
        if (host) {
            connectionToServer("localhost", 5555, new String[]{"finishGame", getCurrentGame().getId().toString()});
        }
    }

    /**
     *  Se inicializa la conexion al servidor y se conecta
     * @param dir la direccion a la que conectarse
     * @param port el puerto al que debera conectarse
     * @param array se le pasara que tipo de juego es
     * @return de un array que se va a recibir del servidor con la direccion y el puerto
     */
    private String[] connectionToServer(String dir, int port, String[] array) {
        String[] arrayReceive = null;
        
        // Crear el socket y conectar con el servidor
        try ( Socket socket = new Socket();) {

            System.out.println("Estableciendo la conexión");
            InetSocketAddress addr = new InetSocketAddress(dir, port);
            socket.connect(addr);
            
            //en el caso de iniciar nueva partida   
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

    /**
     * Se le envia un mensaje al servidor de iniciar y finalizar partida
     * @param socket se le pasa el socket donde escribir el mensaje
     * @param args es el array con los datos del jugador que el vamos a enviar al servidor
     */
    private void sendMessage(Socket socket, String[] args) {
        
        // Envía los mensajes de iniciar y finalizar partida
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
    
    /**
     * Recibiremos el mensaje que nos enviara el servidor con el nickName, la direccion y el puerto
     * @param socket es el socket de donde recibiremos el mensaje
     * @return  nos devuelve la direccion y el puerto del jugador
     */
    private String[] receiveData(Socket socket) {
        List<String> data = new ArrayList<>();
        String linea;
        String[] conversion = null;
        
        // Recoge e imprime los datos de los jugadores de la partida
        try ( InputStream is = socket.getInputStream();  
                InputStreamReader isr = new InputStreamReader(is);  
                BufferedReader bReader = new BufferedReader(isr)) {
            
            while ((linea = bReader.readLine()) != null) {
                data.add(linea);
            }
            
            //convierte en array con la direccion y el puerto del jugador anfitrion
            conversion = conversor(data.get(0));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conversion;
    }
    
    
    
    private void gameLoop(String string, int parseInt) {
        //No toca
    }

    /**
     * Convertiremos el String recibido por el servidor en una array con los datos requeridos
     * @param datosRecibidos aqui vienen los datos que nos envia el servidor en un solo string
     * @return del array con los datos que necesitaremos, que sera la direccion y el puerto
     */
    private String[] conversor(String datosRecibidos) {
        
        //Va a recibir "Nickname: %s Dirección: %s Puerto: %d"
        String[] arrayDatos = datosRecibidos.split(" ");
        
        return new String[]{arrayDatos[3],arrayDatos[5]};
    }
}
