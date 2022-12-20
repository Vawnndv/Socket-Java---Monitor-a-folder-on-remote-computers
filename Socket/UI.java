package Socket;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class UI {
    public List <Socket>listClient = new ArrayList<>();
    ServerSocket s;

    public UI(ServerSocket ss) throws IOException {
        s = ss;
        UI();
    }


    public void UI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        JTextField jTextField = new JTextField();
        jTextField.setText("jTextField1");
        jTextField.setColumns(10);
        // Xu ly su kien
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int port = listClient.get(jList.getSelectedIndex()).getPort();
                jTextField.setText(String.valueOf(port));

                try {
                    TCPServer ts = new TCPServer(s, listClient.get(jList.getSelectedIndex()));
                    ts.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        jPanel.add(new JScrollPane(jList));
        jPanel.add(jTextField);
        frame.add(jPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void runUI() throws IOException {
        do {
            System.out.println("Waiting for a Client");
            Socket ss = s.accept();
            listClient.add(ss);
            System.out.println("Talking to client");
            System.out.println(ss.getPort());
            ListClient lc = new ListClient(jList, listClient);
            lc.start();

        } while (true);
    }

    private  JList jList = new JList();
}
