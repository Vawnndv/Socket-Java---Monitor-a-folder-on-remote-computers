package Socket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ListAction extends Thread {
    DefaultListModel model = new DefaultListModel();
    JList list = new JList(model);
    List <String> listAction = new ArrayList<>();
    Socket s;
    ListAction(JList l, Socket s_) {

        list = l;
        s = s_;
    }
    public void run () {
        try {
            OutputStream os = s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            InputStream is = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String receivedMessage = "";
            do  {
                receivedMessage = br.readLine();
                listAction.add(receivedMessage);
                System.out.println("Status received client ("+ s.getPort() + "): " + receivedMessage);
                String finalReceivedMessage = receivedMessage;

                bw.write("Received");
                bw.newLine();
                bw.flush();
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        String data[] = new String [listAction.size()];
                        for(int i = 0; i < listAction.size(); i++) {
                            data[i] = listAction.get(i);
                        }
                        list.setListData(data);
                    }
                });
            } while (true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
