package Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WatchingFolder extends JFrame{
    Socket s;
    ServerSocket ss;
    DefaultListModel model = new DefaultListModel();
    private  JList j = new JList(model);

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

        JPanel jPanel = new JPanel();


        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        title.add(label);
        title.add(label_name);
        jPanel.add(title);
        jPanel.add(new JScrollPane(j));

        this.add(jPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(700, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Status of Folder");
        process();
    }

    public ListAction la;

    public void process() throws IOException {
        la = new ListAction(j, s);
        la.start();
    }

    private List<String> listFolder = new ArrayList<>();
}
