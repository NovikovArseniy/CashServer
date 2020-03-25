import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PersonalSocket extends Thread {
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private Server server;
    private boolean stop = false;
    public PersonalSocket(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        try {
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        start();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run(){
        String str;
        try{
            //outStream.writeUTF("connected");
            //outStream.flush();
            while(!stop){
                str = inStream.readUTF();
                if (str.equals("exit")) {
                    stop = true;
                    break;
                }
                else {
                    str = server.runCommand(str);
                }
                outStream.writeUTF(str);
                outStream.flush();

            }
        } catch (IOException exc){
            exc.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException exc){
                System.out.println("Disconnected");
            }
        }
    }

    public void close() throws IOException{
        socket.close();
        stop = true;
    }
}
