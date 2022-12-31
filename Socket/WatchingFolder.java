package Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WatchingFolder extends JFrame implements ActionListener{
    Socket s;
    ServerSocket ss;
    DefaultListModel model = new DefaultListModel();
    private  JList j = new JList(model);
    JButton btnDisconnet;

    WatchingFolder (Socket s_, String folder_name) throws IOException {
        s = s_;

        JLabel label = new JLabel("Watching Folder (Port: " + s.getPort() + ")");
        label.setForeground(Color.orange);
        label.setFont(new Font("Gill Sans MT", Font.ITALIC, 40));
        label.setAlignmentX(CENTER_ALIGNMENT);

        JLabel label_name = new JLabel("Folder: " + folder_name);
        label_name.setForeground(Color.green);
        label_name.setFont(new Font("Gill Sans MT", Font.ITALIC, 10));
        label_name.setAlignmentX(CENTER_ALIGNMENT);

        btnDisconnet = new JButton("Disconnect");
        btnDisconnet.addActionListener(this);

        JPanel jPanel = new JPanel();


        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        title.add(label);
        title.add(label_name);
        jPanel.add(title);
        jPanel.add(new JScrollPane(j));
        jPanel.add(btnDisconnet);

        this.add(jPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(700, 400);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Status of Folder");
        process();
    }

    public ListAction la;

    public void process() throws IOException {
        la = new ListAction(j, s);
        la.start();
    }

    private List<String> listFolder = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDisconnet) {
            this.dispose();
            OutputStream os = null;
            try {
                os = s.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                bw.write("quit");
                bw.newLine();
                bw.flush();
            } catch (IOException ex) {

            }
        }
    }
}
