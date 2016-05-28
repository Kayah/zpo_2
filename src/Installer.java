import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.prefs.Preferences;

/**
 * Created by sss on 24.03.16.
 */
public class Installer {

    private static final String OS_NAME = System.getProperty("os.name");
    private static final String OS_VERSION = System.getProperty("os.version");
    private static final String USER_NAME = System.getProperty("user.name");
    private static final double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public static void output(int hashCode) {
        File file = new File("hash.txt");

        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                out.print(hashCode);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void install(String nameFrom, String nameTo) throws Exception{
        FileChannel source = new FileInputStream(new File(nameFrom)).getChannel();
        FileChannel dest = new FileOutputStream(new File(nameTo)).getChannel();
        try{
            source.transferTo(0, source.size(), dest);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            source.close();
            dest.close();
        }
        System.out.println("Successfully installed !!!");
    }

    public static void main(String[] args)throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("OS : " + OS_NAME + "\n");
        System.out.println("OS Version : " + OS_VERSION +"\n");
        System.out.println("Screen width : " + WIDTH + "\n");
        System.out.println("Username : " + USER_NAME + "\n");
        InetAddress address = InetAddress.getLocalHost();
        String hostname = address.getHostName();
        System.out.println("Hostname " + hostname);
        System.out.println("----------------------");
        System.out.println("Enter secret key");
        String userKey = scanner.next();
        String toHash = OS_NAME+OS_VERSION+USER_NAME+WIDTH+hostname+userKey;
        //toHash.hashCode();
        File[] listDisk = File.listRoots();
        for (File f: listDisk){
            System.out.println("Path : " + f.getPath());
            System.out.println("Volume = " + f.getTotalSpace()/1_000_000_000+"GB");
            output(toHash.hashCode());

        }
        Preferences preferences = Preferences.userRoot().node("sss");
        preferences.putInt("12345", toHash.hashCode());

        System.out.println("Enter installation path:");
        String directory = scanner.next();
        String fileOut = directory + "/lab2.jar";
        String fileName = "Data/zpo_1.jar";
        install(fileName, fileOut);

    }
}
