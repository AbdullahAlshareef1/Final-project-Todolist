package database;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TodoItem {
    private int id;
    private String description;
    private boolean done;
    private final LocalDateTime createdAt;
    private LocalDate dueDate;
    private String priority;

    public TodoItem(String description, LocalDate dueDate, String priority) {
        this.description = description;
        this.done = false;
        this.createdAt = LocalDateTime.now();
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public boolean isDone() { return done; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDate getDueDate() { return dueDate; }
    public String getPriority() { return priority; }

    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setDone(boolean done) { this.done = done; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "[" + (done ? "✓" : " ") + "] ID:" + id +
               " | " + description +
               " | Due: " + dueDate +
               " | Priority: " + priority;
    }
}
