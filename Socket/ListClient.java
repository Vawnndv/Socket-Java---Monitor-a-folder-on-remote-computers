package Socket;

import java.net.Socket;
import java.util.List;
import javax.swing.*;

public class ListClient extends Thread {
    List <Socket> listClient;
    JList list;
    ListClient(JList l, List <Socket> s) {
        list = l;
        listClient = s;
    }
    public void run () {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    String data[] = new String [listClient.size()];
                    for(int i = 0; i < listClient.size(); i++) {
                        data[i] = listClient.get(i).toString();
                    }
                    list.setListData(data);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
