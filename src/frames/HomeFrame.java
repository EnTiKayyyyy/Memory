package frames;

import services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionListener;
import entity.User;

public class HomeFrame extends JFrame {
    private Client client;

    public HomeFrame(Client client) {
        this.client = client;  // Dùng client được truyền từ main, không khởi tạo lại
        setTitle("Memory Game - Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Memory Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Lấy dữ liệu người dùng từ client
        List<User> users = client.fetchUserData();

        // Chuyển dữ liệu thành Object[][] để hiển thị trong JTable
        Object[][] userData = convertUserData(users);
        String[] columnNames = {"Username", "Status"};

        JTable playerTable = new JTable(userData, columnNames);
        playerTable.setFillsViewportHeight(true);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        playerTable.setRowHeight(25);
        playerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        playerTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("Online".equals(value)) {
                    cell.setForeground(Color.GREEN);
                } else if ("Offline".equals(value)) {
                    cell.setForeground(Color.RED);
                }
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Player Information"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(scrollPane, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(buttonPanel, gbc);

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 0, 10, 0);
        buttonGbc.gridx = 0;
        buttonGbc.gridy = GridBagConstraints.RELATIVE;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 1;

        JButton playButton = createButton("Play", e -> onPlayButtonClick());
        buttonPanel.add(playButton, buttonGbc);

        JButton leaderboardButton = createButton("Leaderboard", e -> onLeaderboardButtonClick());
        buttonPanel.add(leaderboardButton, buttonGbc);

        JButton logoutButton = createButton("Logout", e -> onLogoutButtonClick());
        buttonPanel.add(logoutButton, buttonGbc);

        JButton exitButton = createButton("Exit", e -> onExitButtonClick());
        buttonPanel.add(exitButton, buttonGbc);

        setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(Color.CYAN);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actionListener);
        return button;
    }

    private void onPlayButtonClick() {
        // Handle Play button click
    }

    private void onLeaderboardButtonClick() {
        setVisible(false);
        new LeaderboardFrame(HomeFrame.this).setVisible(true);
    }

    private void onLogoutButtonClick() {
        // Handle Logout button click
    }

    private void onExitButtonClick() {
        System.exit(0);  // Thoát chương trình
    }

    private Object[][] convertUserData(List<User> users) {
        Object[][] data = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i).getUsername();
            data[i][1] = users.get(i).getStatus();
        }
        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.startConnection("127.0.0.1", 12345);  // Kết nối tới server
            new HomeFrame(client);
        });
    }
}
