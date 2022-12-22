package Socket;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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

    public boolean checkClientIsLie (Socket s) {
        try {
            OutputStream os=s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write("testConnect");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void UI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel jPanel = new JPanel();
        JPanel jpOption = new JPanel();
        jpOption.setLayout(new GridLayout(2,1,50,10));

        JTextField jTextField = new JTextField();
        jTextField.setText("");
        jTextField.setColumns(10);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() ==  btnConnect) {
                    new GetFolder(listClient.get(jList.getSelectedIndex()));
                }
            }
        });

        // Xu ly su kien
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!checkClientIsLie(listClient.get(jList.getSelectedIndex()))) {
                    listClient.remove(jList.getSelectedIndex());
                    ListClient lc = new ListClient(jList, listClient);
                    lc.start();
                    return;
                }

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
        jpOption.add(jTextField);
        jpOption.add(btnConnect);
        jPanel.add(jpOption);
        frame.add(jPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(300, 300);
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
