import java.util.HashMap;
import java.util.Scanner;

public class CashServer {
    public static HashMap<Integer, Integer> serverMap = new HashMap<>();
    //public static final int port = 10500;
    public static void main(String[] args) {
        serverMap.put(0, 10500);
        serverMap.put(1, 10501);
        Scanner scanner = new Scanner(System.in);
        int serverId = scanner.nextInt();
        int port = serverMap.get(serverId);
        Server server = new Server(serverId, port);
        String close = "";
        while (!close.equals("close")){
            close = scanner.next();
            System.out.println(close);
        }
        server.close();
    }
}
