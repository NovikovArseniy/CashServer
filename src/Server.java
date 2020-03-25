import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server extends Thread {

    private int port;
    private int serverId;
    private LinkedList<PersonalSocket> socketList;
    private ServerRepository serverRepository;
    private boolean stop = false;

    public Server(int serverId, int port){
        this.serverId = serverId;
        this.port = port;
        socketList = new LinkedList<>();
        serverRepository = new ServerRepository();
        start();
    }

    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("connected to port " + port);
            try {
                while (!stop) {
                    Socket socket = serverSocket.accept();
                    try{
                        socketList.add(new PersonalSocket(socket, this));
                    }
                    catch (Exception exc){
                        socket.close();
                    }
                }
            } finally {
                serverSocket.close();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public String runCommand(String command){
        if (command.startsWith("insert ")){
            int idCount = 7;
            int id;
            while (command.charAt(idCount) >= '0' && command.charAt(idCount) <='9'){
                idCount++;
            }
            try {
                id = Integer.parseInt(command.substring(7, idCount));
            } catch (Exception exc){
                return "Invalid ID";
            }
            int anotherServerId = findServerId(id);
            if (anotherServerId != serverId){
                ConnectToAnotherServer connectToAnotherServer = new ConnectToAnotherServer(anotherServerId, command);
                try {
                    connectToAnotherServer.join();
                } catch (InterruptedException exc){
                    return "Interrupted connection";
                }
                return connectToAnotherServer.getMessage();
            } else return serverRepository.insertData(id, command.substring(idCount + 1));
        } else if (command.startsWith("getdata ")){
            int id;
            try {
                id = Integer.parseInt(command.substring(8));
            } catch (Exception exc){
                return "Invalid ID";
            }
            int anotherServerId = findServerId(id);
            if (anotherServerId != serverId){
                ConnectToAnotherServer connectToAnotherServer = new ConnectToAnotherServer(anotherServerId, command);
                try {
                    connectToAnotherServer.join();
                } catch (InterruptedException exc){
                    return "Interrupted exception";
                }
                return connectToAnotherServer.getMessage();
            }
            return serverRepository.getData(id);
        } else if (command.startsWith("delete ")){
            int id;
            try {
                id = Integer.parseInt(command.substring(7));
            } catch (Exception exc){
                return "Invalid ID";
            }
            int anotherServerId = findServerId(id);
            if (anotherServerId != serverId){
                ConnectToAnotherServer connectToAnotherServer = new ConnectToAnotherServer(anotherServerId, command);
                try {
                    connectToAnotherServer.join();
                } catch (InterruptedException exc){
                    return "Interrupted exception";
                }
                return connectToAnotherServer.getMessage();
            }
            return serverRepository.deleteData(id);
        } else return "Invalid command";
    }

    private Integer findServerId(Integer id){
        return id % 2;
    }

    public void close(){
        for (PersonalSocket ps : socketList){
            try {
                ps.close();
            } catch (IOException exc){
                System.out.println("Socket close error");
            }
        }
        stop = true;
    }

}
