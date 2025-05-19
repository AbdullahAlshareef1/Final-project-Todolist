package network;

import database.TaskService;
import database.TodoItem;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final TaskService service;
    private static final ReentrantLock lock = new ReentrantLock();

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
                        try {
                            lock.lock();
                            String desc = parts[1];
                            LocalDate dueDate = LocalDate.parse(parts[2]);
                            String priority = parts[3];
                            service.create(new TodoItem(desc, dueDate, priority));
                            out.println("Task added.");
                        } finally {
                            lock.unlock();
                        }
                        break;

                    case "LIST":
                        try {
                            lock.lock();
                            List<TodoItem> tasks = service.findAll();
                            if (tasks.isEmpty()) out.println("No tasks.");
                            else tasks.forEach(task -> out.println(task.toString()));
                        } finally {
                            lock.unlock();
                        }
                        break;

                    case "COMPLETE":
                        try {
                            lock.lock();
                            int compId = Integer.parseInt(parts[1]);
                            out.println(service.complete(compId) ? "Task completed." : "Not found.");
                        } finally {
                            lock.unlock();
                        }
                        break;

                    case "DELETE":
                        try {
                            lock.lock();
                            int delId = Integer.parseInt(parts[1]);
                            out.println(service.remove(delId) ? "Task deleted." : "Not found.");
                        } finally {
                            lock.unlock();
                        }
                        break;

                    case "EDIT":
                        try {
                            lock.lock();
                            int editId = Integer.parseInt(parts[1]);
                            String newDesc = parts[2];
                            LocalDate newDate = LocalDate.parse(parts[3]);
                            String newPriority = parts[4];
                            out.println(service.updateTask(editId, newDesc, newDate, newPriority) ? "Updated." : "Not found.");
                        } finally {
                            lock.unlock();
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
