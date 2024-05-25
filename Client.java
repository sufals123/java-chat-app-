import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 1238);
            System.out.println("Connection established...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startReading() {
        System.out.println("Start reading...");
        Runnable r1 = () -> {
            try {
                String msg;
                while ((msg = br.readLine()) != null) {

                    System.out.println("Server: " + msg);

                    if (msg.equals("exit")) {
                        System.out.println("Server closed the connection");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    private void startWriting() {
        System.out.println("Start writing...");
        Runnable r2 = () -> {
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String content;
                while ((content = br1.readLine()) != null) {
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
         new Client();
    }
}
