package services;
import repository.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.User;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        System.out.println("Server started, waiting for clients...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Lớp xử lý yêu cầu từ client
class ClientHandler extends Thread {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            String message;
            while ((message = (String) in.readObject()) != null) {
                System.out.println("Received from client: " + message);
                String[] parts = message.split(" "); // Split command and parameters

                if (parts[0].equals("register")) {
                    String username = parts[1];
                    String password = parts[2];
                    if (registerUser(username, password)) {
                        out.writeObject("Registration successful!");
                    } else {
                        out.writeObject("Registration failed. Username already exists.");
                    }
                } else if (parts[0].equals("login")) {
                    String username = parts[1];
                    String password = parts[2];
                    if (loginUser(username, password)) {
                        this.username = username; // Store the username for later use
                        out.writeObject("Login successful!");
                    } else {
                        out.writeObject("Login failed. Invalid credentials.");
                    }
                } else if (parts[0].equals("bye")) {
                    break;
                } else if (message.equals("GET_USER_DATA")) {
                    List<User> users = getUsersFromDatabase(); // Lấy dữ liệu từ database
                    out.writeObject(users); // Gửi danh sách User về client
                    out.flush();
                }
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (username != null) {
                updateStatusToOffline(username);
            }
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected.");
        }
    }


    // Đăng ký người dùng mới
    private boolean registerUser(String username, String password) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            // Check if the username already exists
            String checkQuery = "SELECT COUNT(*) FROM players WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return false; // Username already exists
            }

            // Insert new user
            String query = "INSERT INTO players (username, password, score, wins, status) VALUES (?, ?, 0, 0, 'Offline')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void updateStatusToOffline(String username) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            String updateStatusQuery = "UPDATE players SET status = 'Offline' WHERE username = ?";
            PreparedStatement updateStatusStatement = connection.prepareStatement(updateStatusQuery);
            updateStatusStatement.setString(1, username);
            updateStatusStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Đăng nhập người dùng
    private boolean loginUser(String username, String password) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            String query = "SELECT * FROM players WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Update user status to Online
                String updateStatusQuery = "UPDATE players SET status = 'Online' WHERE username = ?";
                PreparedStatement updateStatusStatement = connection.prepareStatement(updateStatusQuery);
                updateStatusStatement.setString(1, username);
                updateStatusStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
    private List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Giả sử bạn có một lớp DatabaseConnection đã tạo sẵn để kết nối DB
            conn = new DatabaseConnection().getConnection();
            stmt = conn.createStatement();
            String query = "SELECT username, status FROM players";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String username = rs.getString("username");
                String status = rs.getString("status");
                users.add(new User(username, status));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}
