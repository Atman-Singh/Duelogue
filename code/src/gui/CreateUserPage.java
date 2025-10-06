package gui;

import networkio.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * Team Project Phase 3 -- CreateUserPage
 * Page displayed when creating a user
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

public class CreateUserPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public CreateUserPage(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;

        // Increase frame size
        setSize(600, 400);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 60, 60));

        // Set up dark look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            defaults.put("control", new Color(27, 27, 27));
            defaults.put("text", Color.WHITE);
            defaults.put("nimbusBase", new Color(25, 25, 25));
            defaults.put("nimbusLightBackground", new Color(35, 35, 35));
            defaults.put("nimbusSelectionBackground", new Color(80, 80, 120));
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Dark Gradient Background Panel
        LoginPage.GradientBackgroundPanel backgroundPanel = new LoginPage.GradientBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // Frosted Glass Panel
        JPanel frostedGlassPanel = new FrostedGlassPanel();
        frostedGlassPanel.setLayout(new GridBagLayout());
        frostedGlassPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25); // Reduced vertical spacing

        // Title Label
        JLabel titleLabel = new JLabel("Create User", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30)); // Slightly smaller font
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5; // Reduce vertical weight
        gbc.anchor = GridBagConstraints.CENTER;
        frostedGlassPanel.add(titleLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;
        gbc.weighty = 0;

        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST; // Right-align labels
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        frostedGlassPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Left-align fields
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20); // Specify preferred width
        usernameField.setBackground(new Color(0, 0, 0));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        frostedGlassPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        frostedGlassPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20); // Specify preferred width
        passwordField.setBackground(new Color(0, 0, 0));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        frostedGlassPanel.add(passwordField, gbc);

        // Buttons
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton createButton = createRoundedButton("Create", new Color(0, 0, 0, 50));
        createButton.addActionListener(e -> createUser());
        frostedGlassPanel.add(createButton, gbc);

        gbc.gridx = 1;
        JButton backButton = createRoundedButton("Back", new Color(0, 0, 0, 50));
        backButton.addActionListener(e -> {
            dispose();
            new LoginPage(oos, ois);
        });
        frostedGlassPanel.add(backButton, gbc);

        // Status Label
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        frostedGlassPanel.add(statusLabel, gbc);

        // Add frosted glass panel to background panel with centered constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(frostedGlassPanel, gbc);

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

    private void createUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        ArrayList<String> data = new ArrayList<>();
        data.add(username);
        data.add(password);

        try {
            oos.writeObject(new Message(Message.MessageType.CREATE_USER, data));
            oos.flush();
            Message response = (Message) ois.readObject();

            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                JOptionPane.showMessageDialog(this, response.getMessage(),
                        "User Creation Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginPage(oos, ois);
            } else {
                statusLabel.setText("Error: " + response.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error connecting to server.");
        }
    }

    // Darker Frosted Glass Panel (identical to LoginPage)
    private class FrostedGlassPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a semi-transparent black background
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            // Add a subtle border
            g2d.setColor(new Color(30, 30, 30, 50));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

            g2d.dispose();

            super.paintComponent(g);
        }
    }
}