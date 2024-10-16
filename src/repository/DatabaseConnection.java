package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/memorygame";  // URL của cơ sở dữ liệu
    private static final String USER = "root";  // Tài khoản MySQL
    private static final String PASSWORD = "123456";  // Mật khẩu MySQL

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Đăng ký MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Kết nối tới cơ sở dữ liệu
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Connection to database failed.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

