package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final Connection conn;

    public TaskService() throws SQLException {
        this.conn = DBManager.connect();
    }

    public synchronized void create(TodoItem item) throws SQLException {
        String sql = "INSERT INTO tasks (title, completed, due_date, priority) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getDescription());
            stmt.setBoolean(2, item.isDone());
            stmt.setDate(3, Date.valueOf(item.getDueDate()));
            stmt.setString(4, item.getPriority());
            stmt.executeUpdate();
        }
    }

    public synchronized List<TodoItem> findAll() throws SQLException {
        List<TodoItem> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY due_date ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TodoItem item = new TodoItem(
                    rs.getString("title"),
                    rs.getDate("due_date").toLocalDate(),
                    rs.getString("priority")
                );
                item.setId(rs.getInt("id"));
                item.setDone(rs.getBoolean("completed"));
                list.add(item);
            }
        }
        return list;
    }

    public synchronized boolean complete(int id) throws SQLException {
        String sql = "UPDATE tasks SET completed = TRUE WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public synchronized boolean remove(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public synchronized boolean updateTask(int id, String newTitle, LocalDate newDueDate, String newPriority) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, due_date = ?, priority = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setDate(2, Date.valueOf(newDueDate));
            stmt.setString(3, newPriority);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        }
    }
}