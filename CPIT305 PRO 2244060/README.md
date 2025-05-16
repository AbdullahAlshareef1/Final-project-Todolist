# 📝 Java Networked Todo List (Server-Client over Sockets)

A multi-user Todo List application using Java Sockets, JDBC, and PostgreSQL. The server handles client commands (add, list, delete, etc.) with thread-safe database access.

## 📦 Features
- Server runs on a defined port
- Clients connect via sockets
- Commands supported: ADD, LIST, COMPLETE, DELETE, EDIT, EXIT
- Synchronized access to database for multi-threaded safety

## 🧱 Database Schema
```sql
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    due_date DATE,
    priority VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🚀 Running
1. Setup PostgreSQL (use pgAdmin or Docker)
2. Create the `tasks` table using the schema above
3. Compile all `.java` files
```bash
javac *.java
```
4. Start the server:
```bash
java Server
```
5. Run multiple clients in new terminals:
```bash
java Client
```

## 👤 Made for CPIT-305 - King Abdulaziz University

## 🤖 AI-Assisted
The AllImportantTests class was created with the help of ChatGPT to verify application functionality and ensure all required test cases are handled.
