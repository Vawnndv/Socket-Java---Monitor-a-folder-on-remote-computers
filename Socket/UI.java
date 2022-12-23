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


public class UI extends JFrame implements ActionListener{
    public List <Socket>listClient = new ArrayList<>();
    ServerSocket s;

    JButton btnConnect;

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
        JPanel jPanel = new JPanel();
        JPanel jpOption = new JPanel();
        jpOption.setLayout(new GridLayout(2,1,50,10));

        JTextField jTextField = new JTextField();
        jTextField.setText("");
        jTextField.setColumns(10);

        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(this);

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

//                try {
//
//                    TCPServer ts = new TCPServer(s, listClient.get(jList.getSelectedIndex()));
//                    ts.start();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
        });

        jPanel.add(new JScrollPane(jList));
        jpOption.add(jTextField);
        jpOption.add(btnConnect);
        jPanel.add(jpOption);
        this.add(jPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() ==  btnConnect) {
            this.dispose();
            try {
                GetFolder gf = new GetFolder(listClient.get(jList.getSelectedIndex()), s);
                gf.process();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
