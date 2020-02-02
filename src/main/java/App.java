
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args) {
    	System.out.println("aaa");
//    	App bc = new App("127.0.0.1",55510);
    	App bc = new App("192.168.1.149",51024);
    	
        try {
        	System.out.println("begin to connect ...");
            bc.connetServer();
        	System.out.println("connected ...");
            bc.sendSingle("Hi from mark.");
        	System.out.println("send ...");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    private String serverHost;
    private int serverPort;
    private Socket socket;
    private OutputStream outputStream;

    public App(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }

    public void connetServer() throws IOException {
        this.socket = new Socket(this.serverHost, this.serverPort);
        this.outputStream = socket.getOutputStream();
        // why the output stream?
    }

    public void sendSingle(String message) throws IOException {
        try {
            this.outputStream.write(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        this.outputStream.close();
        this.socket.close();
    }

}
