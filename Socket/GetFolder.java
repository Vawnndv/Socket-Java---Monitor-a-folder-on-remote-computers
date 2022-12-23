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

public class GetFolder extends JFrame implements ActionListener{
    Socket s;
    ServerSocket ss;
    JButton btnMore,btnChoose;
    GetFolder (Socket s_, ServerSocket ss_) {
        s = s_;
        ss = ss_;

        JLabel label = new JLabel("Choose Folder (Port: " + s.getPort() + ")");
        label.setForeground(Color.orange);
        label.setFont(new Font("Gill Sans MT", Font.ITALIC, 40));
        label.setAlignmentX(CENTER_ALIGNMENT);

        JPanel jFolder = new JPanel();
        JPanel jpOption = new JPanel();
        jpOption.setLayout(new GridLayout(3,1,50,10));

        JTextField jTextField = new JTextField();
        jTextField.setText("");
        jTextField.setColumns(10);

        btnMore = new JButton("More");
        btnMore.addActionListener(this);

        btnChoose = new JButton("Choose");
        btnChoose.addActionListener(this);


        // Xu ly su kien
        j.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e){
                String folder = listFolder.get(j.getSelectedIndex());
                jTextField.setText(folder);
            }
        });

        jFolder.add(label);
        jFolder.add(new JScrollPane(j));
        jpOption.add(jTextField);
        jpOption.add(btnMore);
        jpOption.add(btnChoose);
        jFolder.add(jpOption);
        this.add(jFolder);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(700, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Folder Management");
    }

    private  JList j = new JList();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnChoose) {
            this.dispose();
            OutputStream os= null;
            try {
                os = s.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                bw.write("choose" + listFolder.get(j.getSelectedIndex()));
                bw.newLine();
                bw.flush();
                new WatchingFolder(s, listFolder.get(j.getSelectedIndex()));
//                bw.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == btnMore) {
            OutputStream os = null;
            try {
                os = s.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                bw.write("more" + listFolder.get(j.getSelectedIndex()));
                bw.newLine();
                bw.flush();
//                bw.close();
                listFolder.clear();
                updateFolder();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public ListFolder lf;
    public void updateFolder () throws IOException {
        InputStream is = s.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String receivedMessage = "";
        do {
            receivedMessage = br.readLine();
            if (receivedMessage.equals("done"))
                break;
            System.out.println("Get folder received client ("+ s.getPort() + "): " + receivedMessage);
            listFolder.add(receivedMessage);
            lf = new ListFolder(j, listFolder);
            lf.start();

        } while (true);
//        br.close();
    }

    public void process() throws IOException {
        OutputStream os = s.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        for (int i = 0;i < 2;i++){
            bw.write("getfolder");
            bw.newLine();
            bw.flush();
        }
//        bw.close();
        updateFolder();
    }

    private List<String> listFolder = new ArrayList<>();
}
