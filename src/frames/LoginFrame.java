package frames;
import services.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;
    private JCheckBox showPasswordCheckBox;

    public LoginFrame(Client client) {
        this.client = client;
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(inputPanel);

        JLabel usernameLabel = new JLabel("Username:");
        inputPanel.add(usernameLabel);

        usernameField = new JTextField();
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        inputPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Nút Show Password
        showPasswordCheckBox = new JCheckBox("Show Password");
        inputPanel.add(showPasswordCheckBox);

        showPasswordCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    passwordField.setEchoChar((char) 0);  // Hiện password
                } else {
                    passwordField.setEchoChar('*');  // Ẩn password
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel);

        JButton loginButton = new JButton("Login");
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        buttonPanel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Gửi yêu cầu đăng nhập tới Server thông qua Client
                String response = client.sendMessage("login " + username + " " + password);
                JOptionPane.showMessageDialog(null, response);

                if (response.equals("Login successful!")) {
                    setVisible(false); // Close the LoginFrame
                    new HomeFrame(new Client()).setVisible(true);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Close the LoginFrame
                new RegisterFrame(client).setVisible(true);
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 12345);
        new LoginFrame(client);
    }
}
