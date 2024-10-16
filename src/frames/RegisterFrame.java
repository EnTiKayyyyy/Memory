package frames;

import services.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;

    public RegisterFrame(Client client) {
        this.client = client;
        setTitle("Register");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(inputPanel);

        JLabel usernameLabel = new JLabel("Username:");
        inputPanel.add(usernameLabel);

        usernameField = new JTextField();
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        inputPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel);

        JButton registerButton = new JButton("Register");
        buttonPanel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Gửi yêu cầu đăng ký tới Server thông qua Client
                String response = client.sendMessage("register " + username + " " + password);
                JOptionPane.showMessageDialog(null, response);

                if (response.equals("Registration successful!")) {
                    setVisible(false); // Close the RegisterFrame
                    new LoginFrame(client).setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 12345);
        new RegisterFrame(client);
    }
}