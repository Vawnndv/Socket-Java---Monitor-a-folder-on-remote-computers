package Socket;

import java.net.ServerSocket;
import java.io.*;
import java.net.*;

public class TCPServer extends Thread{
    private Thread t;
    Socket ss= null; //synchronous

    ServerSocket s;
    int port;

    TCPServer (ServerSocket s_, Socket ss_) throws IOException {
        ss = ss_;
        s = s_;
        port = ss.getPort();
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        InputStream is= null;
        try {
            is = ss.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            OutputStream os=ss.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            String receivedMessage;

            do
            {
                receivedMessage=br.readLine();
                System.out.println("Received client ("+ port + "): " + receivedMessage);
                if (receivedMessage.equalsIgnoreCase("quit"))
                {
                    System.out.println("Client has left !");
                    break;
                }
                else
                {
                    System.out.println("Send client ("+ port + "): ");
                    DataInputStream din=new DataInputStream(System.in);
                    String k=din.readLine();
                    bw.write(k);
                    bw.newLine();
                    bw.flush();
                }
            }
            while (true);
            bw.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

