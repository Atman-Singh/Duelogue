package gui;


import database.ArgumentTopic;
import database.User;
import networkio.Message;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Team Project Phase 3 -- FeedPage
 * Page displayed when viewing the news feed
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

class FeedPage extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private String username;
    private JPanel feedPanel;
    private JScrollPane feedScrollPane;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public FeedPage(String username, ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        this.username = username;
        setTitle("DueLogue");


        // Adjusted frame size
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        // Modern Dark Theme Setup
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();


            // Refined color palette
            Color darkBackground = new Color(22, 22, 29);
            Color midBackground = new Color(29, 29, 38);
            Color accentColor = new Color(94, 108, 204);
            Color textColor = new Color(220, 220, 230);
            Color subtleText = new Color(150, 150, 170);


            defaults.put("control", darkBackground);
            defaults.put("text", textColor);
            defaults.put("Panel.background", midBackground);
            defaults.put("Label.foreground", textColor);
            defaults.put("Button.background", accentColor);
            defaults.put("Button.foreground", Color.WHITE);
            defaults.put("nimbusFocus", new Color(120, 130, 220));
            defaults.put("nimbusSelectionBackground", new Color(110, 120, 240));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Contemporary Typography
        Font mainFont = new Font("Inter", Font.PLAIN, 14);
        Font titleFont = new Font("Inter", Font.BOLD, 16);


        // Gradient Background Panel with Modern Design
        ModernGradientBackgroundPanel backgroundPanel = new ModernGradientBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);


        // Card layout for main content
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);


        // Profile Panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setOpaque(false);
        JLabel profileLabel = new JLabel("User Profile: " + username, JLabel.CENTER);
        profileLabel.setFont(titleFont);
        profileLabel.setForeground(Color.WHITE);
        profilePanel.add(profileLabel, BorderLayout.CENTER);


        // News Feed Panel
        feedPanel = new JPanel(new BorderLayout());


        feedPanel.setOpaque(false);
        feedScrollPane = new JScrollPane();
        feedScrollPane.getViewport().setOpaque(false);
        feedScrollPane.setOpaque(false);
        feedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        feedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        feedPanel.add(feedScrollPane, BorderLayout.CENTER);


        // Add panels to card layout
        mainPanel.add(feedPanel, "Feed");
        mainPanel.add(profilePanel, "Profile");


        // Button Panel with improved styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBackground(new Color(30, 30, 30, 200));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));


        // Buttons with consistent styling
        JButton[] buttons = {
                createStyledButton("View Profile", mainFont),
                createStyledButton("Make Argument Topic", mainFont),
                createStyledButton("View Feed", mainFont),
                createStyledButton("Add Friends", mainFont),
                createStyledButton("Logout", mainFont)


        };


        // Button actions
        buttons[0].addActionListener(e ->{
            try {
                new UserPage(username, username, ois, oos);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttons[1].addActionListener(e -> {
            dispose();
            populateFeed(feedPanel, feedScrollPane);
            new MakeArgumentTopicPage(username, oos, ois);});
        buttons[4].addActionListener(e -> {
            try {
                oos.writeObject(new Message(Message.MessageType.SAVE_DATA, ""));
                oos.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            dispose();
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                window.dispose();
            }
        });
        buttons[3].addActionListener(e -> showAddFriendPanel(username));


        // Add buttons to button panel
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        buttons[2].addActionListener(e -> {
            dispose();
            new FeedPage(username, oos, ois);
            populateFeed(feedPanel, feedScrollPane);
        });


        // Add buttons to button panel
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        // Main layout
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);


        // Populate feed
        populateFeed(feedPanel, feedScrollPane);


        // Final setup
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    // Improved button creation method
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 90));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 90, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 90));
            }
        });


        return button;
    }


    private void populateFeed(JPanel feedPanel, JScrollPane scrollPane) {
        ArrayList<String> data = new ArrayList<>();


        feedPanel.add(scrollPane, BorderLayout.CENTER);
        data.add(username);


        try {
            // Retrieve the current user details
            oos.writeObject(new Message(Message.MessageType.GET_USER, data));
            oos.flush();
            Message gotUser = (Message) ois.readObject();
            User user = (User) gotUser.getData().get(0);


            if (user.getFriendlist().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "In order to view the news feed, you must add friends",
                        "DueLogue",
                        JOptionPane.ERROR_MESSAGE
                );
                showAddFriendPanel(username);
                return;
            }


            oos.writeObject(new Message(Message.MessageType.VIEW_NEWS_FEED, data));
            oos.flush();
            Message response = (Message) ois.readObject();


            if (response.getMessageType() == Message.MessageType.ERROR) {
                JLabel errorLabel = new JLabel("Error: " + response.getMessage());
                errorLabel.setForeground(Color.WHITE);
                feedPanel.add(errorLabel, BorderLayout.CENTER);
            } else {
                ArrayList<ArgumentTopic> feedData = (ArrayList<ArgumentTopic>) response.getData().get(0);
                feedPanel.removeAll();


                JPanel topicsPanel = new JPanel();
                topicsPanel.setLayout(new BoxLayout(topicsPanel, BoxLayout.Y_AXIS));
                topicsPanel.setBackground(new Color(30, 30, 30));


                for (ArgumentTopic topic : feedData) {
                    JPanel topicPanel = new JPanel(new BorderLayout());
                    topicPanel.setBackground(new Color(45, 45, 45));
                    topicPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));


                    // Title as a button with wrapped text
                    JButton titleButton = createRoundedButton(topic.getTitle(), new Font("Roboto", Font.BOLD, 18));
                    ImageIcon img = new ImageIcon(topic.getImagepath());
                    JLabel imgLabel = new JLabel(img);
                    titleButton.setHorizontalAlignment(SwingConstants.LEFT);
                    titleButton.addActionListener(e -> {new ArgumentTopicPage(topic, username, oos, ois); populateFeed(feedPanel, feedScrollPane);});


                    // Content as a wrapped label
                    JTextArea contentArea = new JTextArea(topic.getContent());




                    contentArea.setLineWrap(true);
                    contentArea.setWrapStyleWord(true);
                    contentArea.setEditable(false);
                    contentArea.setBackground(new Color(143, 143, 143));
                    contentArea.setForeground(new Color(0,0,0,255));
                    contentArea.setFont(new Font("Roboto", Font.PLAIN, 14));


                    // Author as a clickable label
                    JLabel authorLabel = new JLabel("By: " + topic.getAuthor().getUsername());
                    authorLabel.setForeground(new Color(120, 120, 120));
                    authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


                    topicPanel.setBackground(new Color(0,0,0,29));
                    topicPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(0, 0, 0, 29), 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    topicPanel.add(imgLabel, BorderLayout.WEST);
                    // Rounded corners for topic panels
                    topicPanel.setBorder(new RoundedBorder(15));






                    authorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            try {
                                new UserPage(username, topic.getAuthor().getUsername(), ois, oos);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


                    topicPanel.add(titleButton, BorderLayout.NORTH);
                    topicPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
                    topicPanel.add(authorLabel, BorderLayout.SOUTH);


                    topicsPanel.add(topicPanel);
                    topicsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between topics
                }


                scrollPane.setViewportView(topicsPanel);
                feedPanel.add(scrollPane, BorderLayout.CENTER);
            }


            feedPanel.revalidate();
            feedPanel.repaint();


        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error retrieving feed.");
            errorLabel.setForeground(Color.WHITE);
            feedPanel.add(errorLabel, BorderLayout.CENTER);
        }
    }


    private JButton createRoundedButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(new Color(0,0,0, 152));
        button.setBorder(new RoundedBorder(10)); // Custom rounded border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }


    private void showAddFriendPanel(String username) {
        try {
            ArrayList data = new ArrayList<>();
            data.add(username);
            oos.writeObject(new Message(Message.MessageType.GET_RANDOM_USERS, data));
            oos.flush();
            Message response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.USER_LIST) {
                ArrayList<String> randomUsers = (ArrayList<String>) response.getData();


                JPanel friendPanel = new JPanel();
                friendPanel.setBackground(new Color(45, 45, 45));
                friendPanel.setLayout(new BorderLayout());
                JLabel label = new JLabel("Friend Suggestions", JLabel.CENTER);
                label.setForeground(Color.WHITE);
                friendPanel.add(label, BorderLayout.NORTH);


                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (String user : randomUsers) {
                    if (!user.equals(username)) {
                        listModel.addElement(user);
                    }


                }
                JList<String> userList = new JList<>(listModel);
                userList.setBackground(new Color(30, 30, 30));
                userList.setForeground(Color.WHITE);
                JScrollPane scrollPane = new JScrollPane(userList);
                friendPanel.add(scrollPane, BorderLayout.CENTER);


                JPanel searchPanel = new JPanel();
                searchPanel.setBackground(new Color(45, 45, 45));
                JTextField searchField = new JTextField(20);
                JButton searchButton = new JButton("Search");
                searchButton.addActionListener(e -> {
                    try {
                        ArrayList userData = new ArrayList();
                        data.add(searchField.getText());
                        oos.writeObject(new Message(Message.MessageType.GET_SEARCH_RESULTS, data));
                        oos.flush();
                        Message in = (Message) ois.readObject();
                        if (in.getMessageType() == Message.MessageType.USER_LIST) {
                            ArrayList<String> suggestedUsers = (ArrayList<String>) in.getData().get(0);
                            listModel.clear();
                            for (String user : suggestedUsers) {
                                listModel.addElement(user);
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Wrong message type",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                });
                friendPanel.add(searchPanel, BorderLayout.NORTH);


                searchField.setBackground(new Color(30, 30, 30));
                searchField.setForeground(Color.WHITE);
                searchButton.setBackground(new Color(60, 63, 65));
                searchButton.setForeground(Color.WHITE);


                searchPanel.add(new JLabel("Search: "));
                searchPanel.add(searchField);
                searchPanel.add(searchButton);


                JButton addFriendButton = createRoundedButton("Add Friend", new Font("Roboto", Font.PLAIN, 14));
                addFriendButton.addActionListener(e -> {
                    String selectedUser = userList.getSelectedValue();
                    if (selectedUser != null) {
                        ArrayList<String> addFriendData = new ArrayList<>();
                        addFriendData.add(username);
                        addFriendData.add(selectedUser);


                        try {
                            oos.writeObject(new Message(Message.MessageType.ADD_FRIEND, addFriendData));
                            oos.flush();
                            Message addResponse = (Message) ois.readObject();
                            if (addResponse.getMessageType() == Message.MessageType.SUCCESS) {
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Friend added successfully!",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                                dispose();
                                new FeedPage(username, oos, ois);
                            } else {
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Error: " + addResponse.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Error adding friend.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
                friendPanel.add(addFriendButton, BorderLayout.SOUTH);


                mainPanel.add(friendPanel, "AddFriend");
                cardLayout.show(mainPanel, "AddFriend");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();


            // Create dark gradient
            GradientPaint gradient = new GradientPaint(
                    50, 50, new Color(46, 12, 0), // Dark Gray
                    getWidth(), getHeight(), new Color(0, 26, 53) // Deep Blue
            );


            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());


            g2d.dispose();
        }
    }
    private class ModernGradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();


            // Sophisticated multi-stop gradient
            Color[] colors = {
                    new Color(22, 22, 29),      // Dark base
                    new Color(29, 29, 38),      // Slightly lighter mid-tone
                    new Color(38, 38, 47)       // Variation in depth
            };


            float[] fractions = {0.0f, 0.5f, 1.0f};


            LinearGradientPaint gradient = new LinearGradientPaint(
                    0, 0,
                    getWidth(), getHeight(),
                    fractions,
                    colors
            );


            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());


            g2d.dispose();
        }
    }




    // Rounded Border class for the buttons
    class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius;


        public RoundedBorder(int radius) {
            this.radius = radius;
        }


        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, width - 10, height - 10, radius, radius);
        }


        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2 + 1, radius/2 + 1, radius/2 + 1, radius/2 + 1);
        }


        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius + 1;
            return insets;
        }
    }


}
