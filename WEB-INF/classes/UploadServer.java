package WEB;

import java.io.IOException;
import java.net.ServerSocket;
public class UploadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8999);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8999.");
            System.exit(-1);
        }
        while (true) {
	    new WEB.UploadServerThread(serverSocket.accept()).start();
        }
    }
}
