package Socket;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String arg[]) throws IOException {
        ServerSocket s = new ServerSocket(3200);
        new UI(s).runUI();
    }
}
