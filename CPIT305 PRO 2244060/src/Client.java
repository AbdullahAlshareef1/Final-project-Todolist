import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(in.readLine());

            while (true) {
                System.out.print("Command: ");
                String cmd = scanner.nextLine();
                out.println(cmd);

                if (cmd.equalsIgnoreCase("EXIT")) break;

                String response;
                while ((response = in.readLine()) != null && !response.isEmpty()) {
                    System.out.println(response);
                    if (!in.ready()) break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}