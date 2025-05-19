import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread for reading from server
            Thread readThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.err.println("Disconnected from server.");
                }
            });

            // Thread for writing to server
            Thread writeThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                try {
                    while (true) {
                        System.out.print("Command: ");
                        String cmd = scanner.nextLine();
                        out.println(cmd);
                        if (cmd.equalsIgnoreCase("EXIT")) {
                            socket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error sending command: " + e.getMessage());
                } finally {
                    scanner.close();
                }
            });

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
