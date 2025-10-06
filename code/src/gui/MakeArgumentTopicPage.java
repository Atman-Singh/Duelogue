package gui;

import networkio.Message;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Team Project Phase 3 -- MakeArgumentTopicPage
 * Page for making a post
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

class MakeArgumentTopicPage extends JFrame {
    private static final Color BACKGROUND_DARK = new Color(20, 20, 25);
    private static final Color COMPONENT_DARK = new Color(30, 30, 40);
    private static final Color TEXT_COLOR = new Color(220, 220, 240);
    private static final Color ACCENT_BLUE = new Color(40, 60, 80);
    private static final Color ACCENT_ORANGE = new Color(70, 50, 40);
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font TEXT_FONT = new Font("Inter", Font.PLAIN, 14);

    private JTextField titleField;
    private JTextField imageField;
    private JTextArea contentArea;
    private JLabel statusLabel;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public MakeArgumentTopicPage(String username, ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;

        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            defaults.put("control", BACKGROUND_DARK);
            defaults.put("text", TEXT_COLOR);
            defaults.put("nimbusBase", new Color(25, 35, 45));
            defaults.put("nimbusFocus", ACCENT_BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        setTitle("DueLogue - Create Argument Topic");
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        LoginPage.GradientBackgroundPanel backgroundPanel = new LoginPage.GradientBackgroundPanel();
        setContentPane(backgroundPanel);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        createComponents(username, gbc);

        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createComponents(String username, GridBagConstraints gbc) {
        
        JLabel titleLabel = createLabel("Title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        titleField = createTextField(20);
        gbc.gridx = 1;
        add(titleField, gbc);

        
        JLabel imageLabel = createLabel("Image:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(imageLabel, gbc);

        imageField = createTextField(40);
        gbc.gridx = 1;
        add(imageField, gbc);

        JButton pickFileButton = createStyledButton("Pick File", ACCENT_BLUE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(pickFileButton, gbc);

        
        JLabel contentLabel = createLabel("Content:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(contentLabel, gbc);

        contentArea = createTextArea("Write your argument content");
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBackground(BACKGROUND_DARK);
        gbc.gridx = 1;
        gbc.gridheight = 2;
        add(scrollPane, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(BACKGROUND_DARK);

        JButton postButton = createStyledButton("Post", ACCENT_BLUE);
        JButton backButton = createStyledButton("Back", ACCENT_ORANGE);

        buttonPanel.add(postButton);
        buttonPanel.add(backButton);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        add(buttonPanel, gbc);

        
        statusLabel = createLabel("");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(statusLabel, gbc);

        
        pickFileButton.addActionListener(e -> pickFile());
        postButton.addActionListener(e -> {
            postArgumentTopic(username);
            dispose();
            new FeedPage(username, oos, ois);


        });
        backButton.addActionListener(e -> dispose());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(LABEL_FONT);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setBackground(COMPONENT_DARK);
        textField.setForeground(TEXT_COLOR);
        textField.setFont(TEXT_FONT);
        return textField;
    }

    private JTextArea createTextArea(String placeholder) {
        JTextArea textArea = new JTextArea(placeholder);

        textArea.setBackground(COMPONENT_DARK);
        textArea.setForeground(TEXT_COLOR);
        textArea.setFont(TEXT_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color buttonColor = getModel().isPressed() ? baseColor.darker() : baseColor;
                g2.setColor(buttonColor);

                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(
                        0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.fill(roundedRectangle);

                g2.setColor(TEXT_COLOR.darker());
                g2.draw(roundedRectangle);

                g2.setColor(TEXT_COLOR);
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

        button.setForeground(TEXT_COLOR);
        button.setFont(LABEL_FONT);
        return button;
    }

    
    private void pickFile() {
        JFrame imageFrame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Extension Filter",
                "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(imageFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            ImageIcon img = new ImageIcon(chooser.getSelectedFile().getPath());
        }
    }

    private void postArgumentTopic(String username) {
        String title = titleField.getText().trim();
        String image = imageField.getText().trim();
        String content = contentArea.getText().trim();

        
        if (title.isEmpty() || image.isEmpty() || content.isEmpty()) {
            statusLabel.setText("Error: All fields are required.");
            return;
        }

        ArrayList data = new ArrayList();
        data.add(title);
        data.add(content);
        data.add(username);


        try {
            oos.writeObject(new Message(Message.MessageType.CREATE_ARGUMENT_TOPIC, data));
            oos.flush();
            Message response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                oos.writeObject(new Message(Message.MessageType.SAVE_DATA, data));
                oos.flush();
                JOptionPane.showMessageDialog(this, "Argument topic posted successfully!");
                dispose();
            } else {
                statusLabel.setText(response.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error connecting to server.");
        }
    }
}