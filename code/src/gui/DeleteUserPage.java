package gui;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import networkio.Message;


/**
 * Team Project Phase 3 -- DeleteUserPage
 * Page displayed when trying to delete a user
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */


class DeleteUserPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public DeleteUserPage(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);

        // Set up the dark theme look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            defaults.put("control", new Color(35, 35, 35));
            defaults.put("text", Color.WHITE);
            defaults.put("nimbusBase", new Color(18, 30, 49));
            defaults.put("nimbusAlertYellow", new Color(127, 104, 31));
            defaults.put("nimbusDisabledText", new Color(128, 128, 128));
            defaults.put("nimbusFocus", new Color(115, 164, 209));
            defaults.put("nimbusGreen", new Color(176, 179, 50));
            defaults.put("nimbusInfoBlue", new Color(66, 139, 221));
            defaults.put("nimbusLightBackground", new Color(18, 30, 49));
            defaults.put("nimbusSelectionBackground", new Color(104, 93, 156));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Custom font
        Font customFont = new Font("Segoe UI", Font.BOLD, 14);
        UIManager.put("Label.font", customFont);
        UIManager.put("TextField.font", customFont);
        UIManager.put("PasswordField.font", customFont);
        UIManager.put("Button.font", customFont);

        // Frame setup
        setTitle("DUELOGUE");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("DUELOGUE", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField();
        usernameField.setBackground(new Color(50, 50, 50));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(50, 50, 50));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        add(passwordField, gbc);

        // Buttons
        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton deleteUserButton = createRoundedButton("Delete database.User", new Color(0, 0, 0));
        deleteUserButton.addActionListener(e -> deleteUser());
        add(deleteUserButton, gbc);

        // Status Label
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        add(statusLabel, gbc);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Custom rounded button method
    private JButton createRoundedButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? backgroundColor.darker() : backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            public void updateUI() {
                super.updateUI();
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
            }
        };
        button.setForeground(Color.WHITE);
        return button;
    }

    private void deleteUser() {
        ArrayList<String> data = new ArrayList<>();
        data.add(usernameField.getText());

        try {
            oos.writeObject(new Message(Message.MessageType.DELETE_USER, data));
            oos.flush();
            Message response = (Message) ois.readObject();

            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                JOptionPane.showMessageDialog(this, response.getMessage(),
                        "Delete database.User Successful", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                new LoginPage(oos, ois); // Assuming you have a gui.LoginPage class
            } else {
                statusLabel.setText("Delete database.User failed: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error connecting to server.");
        }
    }
}