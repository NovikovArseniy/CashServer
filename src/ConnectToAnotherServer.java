import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectToAnotherServer extends Thread {
    private String command;
    private Socket socket;
    private DataInputStream serverIn;
    private DataOutputStream serverOut;
    private String message;

    public ConnectToAnotherServer(int serverId, String command){
        this.command = command;
        try {
            socket = new Socket("localhost", CashServer.serverMap.get(serverId));
            serverIn = new DataInputStream(socket.getInputStream());
            serverOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException exc){
            exc.printStackTrace();
        }
        start();
    }

    @Override
    public void run(){
        try {
            serverOut.writeUTF(command);
            serverOut.flush();
            message = serverIn.readUTF();
        } catch (IOException exc){
            exc.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    public String getMessage(){
        return message;
    }
}
