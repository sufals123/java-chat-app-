import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(1238); // Specify the port number
            System.out.println("Waiting for client...");
            socket = server.accept();
            System.out.println("Client connected.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // Auto flush

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
                while (true) {
                    msg = br.readLine();
                    if (msg == null || msg.equalsIgnoreCase("exit")) {
                        System.out.println("Client disconnected.");
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            } finally {
                closeResources();
            }
        };
        new Thread(r1).start();
    }

    private void startWriting() {
        System.out.println("Start writing...");
        Runnable r2 = () -> {
            try (BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in))) {
                String content;
                while (true) {
                    content = br1.readLine();
                    if (content.equalsIgnoreCase("exit")) {
                        out.println("exit");
                        closeResources();
                        break;
                    }
                    out.println(content);
                }
            } catch (Exception e) {
                System.out.println("Error writing to client.");
            }
        };
        new Thread(r2).start();
    }

    private void closeResources() {
        try {
            if (br != null) br.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (server != null) server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Resources closed, server shutting down.");
    }

    public static void main(String[] args) {
        System.out.println("Server starting...");
        new Server(); // Start the server by creating an instance of Server
    }
}
