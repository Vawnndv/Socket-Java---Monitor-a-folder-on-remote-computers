package Socket;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class TCPClient
{
    public static void main(String arg[])
    {
        try
        {
            Socket s = new Socket("localhost",3200);
            System.out.println(s.getPort());

            InputStream is=s.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            OutputStream os=s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String sentMessage="";
            String receivedMessage = "";
            System.out.println("Talking to Server");

            String path = "D:\\testChange";
            System.out.println("Watching directory for changes");
            // STEP1: Create a watch service
            WatchService watchService = FileSystems.getDefault().newWatchService();

            // STEP2: Get the path of the directory which you want to monitor.
            Path directory = Path.of(path);

            // STEP3: Register the directory with the watch service
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            do
            {

//                DataInputStream din=new DataInputStream(System.in);
//                sentMessage=din.readLine();
//                bw.write(sentMessage);
//                bw.newLine();
//                bw.flush();

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    sentMessage = "";
                    // STEP5: Get file name from even context
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                    Path fileName = pathEvent.context();

                    // STEP6: Check type of event.
                    WatchEvent.Kind<?> kind = event.kind();
                    File file = new File (path + "\\" + fileName);
                    boolean isDirectory = file.isDirectory();
                    boolean isFile = file.isFile();
                    // STEP7: Perform necessary action with the event
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (isFile)
                            sentMessage = "A new file is created : " + fileName;
                        else if (isDirectory)
                            sentMessage = "A new folder is created : " + fileName;
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
//                        if (isFile)
//                            System.out.println("A file has been deleted: " + fileName);
//                        else if (isDirectory)
//                            System.out.println("A folder has been deleted: " + fileName);
                        sentMessage = "A file/folder has been deleted: " + fileName;
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (isFile)
                            sentMessage = "A file has been modified: " + fileName;
                        else if (isDirectory)
                            sentMessage = "A folder has been modified: " + fileName;
                    }
                    bw.write(sentMessage);
                    bw.newLine();
                    bw.flush();
                    System.out.println("Sent Server: " + sentMessage);

                    if (receivedMessage.equalsIgnoreCase("quit"))
                        break;
                    else
                    {
                        receivedMessage=br.readLine();
                        System.out.println("Received : " + receivedMessage);
                    }
                }

                // STEP8: Reset the watch key everytime for continuing to use it for further event polling
                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }
            }
            while(true);

            bw.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
}
