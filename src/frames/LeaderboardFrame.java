package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import repository.DatabaseConnection;
import frames.HomeFrame;  // Thêm import
import services.Client;  // Thêm import

public class LeaderboardFrame extends JFrame {

    private JTable leaderboardTable;
    private JButton backButton;
    private DatabaseConnection dbConnection;
    private HomeFrame homeFrame;  // Thêm biến HomeFrame

    // Sửa constructor để nhận HomeFrame
    public LeaderboardFrame(HomeFrame homeFrame) {
        this.homeFrame = homeFrame;  // Lưu lại tham chiếu đến HomeFrame

        setTitle("Leaderboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Initialize database connection
        dbConnection = new DatabaseConnection();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Leaderboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table data
        String[] columnNames = {"Rank", "Username", "Score"};
        Object[][] data = getLeaderboardData(); // Get leaderboard data from database

        leaderboardTable = new JTable(data, columnNames);
        leaderboardTable.setEnabled(false); // Table non-editable
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane tableScrollPane = new JScrollPane(leaderboardTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(backButton, BorderLayout.SOUTH);

        // Back button action listener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close LeaderboardFrame
                homeFrame.setVisible(true);  // Hiển thị lại HomeFrame
            }
        });

        add(mainPanel);
    }

    // Method to retrieve leaderboard data from database
    private Object[][] getLeaderboardData() {
        ArrayList<Object[]> leaderboardData = new ArrayList<>();
        try {
            Connection conn = dbConnection.getConnection();
            String query = "SELECT username, score FROM players ORDER BY score DESC LIMIT 10"; // Example query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                leaderboardData.add(new Object[]{rank, username, score});
                rank++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboardData.toArray(new Object[0][0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeaderboardFrame(new HomeFrame(new Client())).setVisible(true));
    }
}
