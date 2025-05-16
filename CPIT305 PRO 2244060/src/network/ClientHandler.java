package network;

import database.TaskService;
import database.TodoItem;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final TaskService service;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        try {
            this.service = new TaskService();
        } catch (SQLException e) {
            throw new IOException("Database error", e);
        }
    }

    @Override
    public void run() {
        try {
            out.println("Welcome to the Todo Server. Commands: ADD, LIST, COMPLETE, DELETE, EDIT, EXIT");

            String input;
            while ((input = in.readLine()) != null) {
                String[] parts = input.split("::");
                String cmd = parts[0].toUpperCase();

                switch (cmd) {
                    case "ADD":
                        String desc = parts[1];
                        LocalDate dueDate = LocalDate.parse(parts[2]);
                        String priority = parts[3];
                        synchronized (service) {
                            service.create(new TodoItem(desc, dueDate, priority));
                        }
                        out.println("Task added.");
                        break;

                    case "LIST":
                        synchronized (service) {
                            List<TodoItem> tasks = service.findAll();
                            if (tasks.isEmpty()) out.println("No tasks.");
                            else tasks.forEach(task -> out.println(task.toString()));
                        }
                        break;

                    case "COMPLETE":
                        int compId = Integer.parseInt(parts[1]);
                        synchronized (service) {
                            out.println(service.complete(compId) ? "Task completed." : "Not found.");
                        }
                        break;

                    case "DELETE":
                        int delId = Integer.parseInt(parts[1]);
                        synchronized (service) {
                            out.println(service.remove(delId) ? "Task deleted." : "Not found.");
                        }
                        break;

                    case "EDIT":
                        int editId = Integer.parseInt(parts[1]);
                        String newDesc = parts[2];
                        LocalDate newDate = LocalDate.parse(parts[3]);
                        String newPriority = parts[4];
                        synchronized (service) {
                            out.println(service.updateTask(editId, newDesc, newDate, newPriority) ? "Updated." : "Not found.");
                        }
                        break;

                    case "EXIT":
                        out.println("Goodbye!");
                        return;

                    default:
                        out.println("Unknown command.");
                }
            }
        } catch (IOException | SQLException e) {
            out.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {}
        }
    }
}