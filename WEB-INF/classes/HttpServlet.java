package WEB;

import java.io.*;
import java.net.Socket;
import java.time.Clock;

public abstract class HttpServlet {
   protected void doGet(WEB.HttpServletRequest request, WEB.HttpServletResponse response) { return; };
   protected void doPost(WEB.HttpServletRequest request, WEB.HttpServletResponse response) { return; };

    public static class Activity {
       private String dirName = null;
       public static void main(String[] args) throws IOException {
          new Activity().onCreate();
       }
       public Activity() {
       }
       public void onCreate() {
          System.out.println(new UploadClient().uploadFile());
       }
    }

    public static class UploadClient {
        public UploadClient() { }
        public String uploadFile() {
            String listing = "";
            try {
                Socket socket = new Socket("localhost", 8999);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream("images/AndroidLogo.png");
                byte[] bytes = fis.readAllBytes();
                out.write(bytes);
                socket.shutdownOutput();
                fis.close();
                System.out.println("Came this far\n");
                String filename = "";
                while ((filename = in.readLine()) != null) {
                    listing += filename;
                }
                socket.shutdownInput();
            } catch (Exception e) {
                System.err.println(e);
            }
            return listing;
        }
    }

    public static class UploadServlet extends HttpServlet {
       protected void doPost(WEB.HttpServletRequest request, WEB.HttpServletResponse response) {
          try {
             InputStream in = request.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             byte[] content = new byte[1];
             int bytesRead = -1;
             while( ( bytesRead = in.read( content ) ) != -1 ) {
                baos.write( content, 0, bytesRead );
             }
             Clock clock = Clock.systemDefaultZone();
             long milliSeconds=clock.millis();
             OutputStream outputStream = new FileOutputStream(new File(String.valueOf(milliSeconds) + ".png"));
             baos.writeTo(outputStream);
             outputStream.close();
             PrintWriter out = new PrintWriter(response.getOutputStream(), true);
             File dir = new File(".");
             String[] chld = dir.list();
               for(int i = 0; i < chld.length; i++){
                String fileName = chld[i];
                out.println(fileName+"\n");
                System.out.println(fileName);
             }
          } catch(Exception ex) {
             System.err.println(ex);
          }
       }
    }
}