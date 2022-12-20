package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RunServer {
    public static void main(String arg[]) throws IOException {
        ServerSocket s = new ServerSocket(3200);
        do {
            System.out.println("Waiting for a Client");
            Socket ss= s.accept(); //synchronous
            System.out.println("Talking to client");
            System.out.println(ss.getPort());
            TCPServer ts = new TCPServer(s, ss);
            ts.start();
        } while (true);
    }
}
