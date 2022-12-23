package Socket;

import java.util.List;
import javax.swing.*;

public class ListFolder extends Thread {
    List <String> listFolder;
    JList list;
    ListFolder(JList l, List <String> s) {
        list = l;
        listFolder = s;
    }
    public void run () {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    String data[] = new String [listFolder.size()];
                    for(int i = 0; i < listFolder.size(); i++) {
                        data[i] = listFolder.get(i);
                    }
                    list.setListData(data);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
