package gui;


import database.ArgumentTopic;
import database.User;
import networkio.Message;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Team Project Phase 3 -- UserPage
 * Displays the page for viewing the user
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

class UserPage extends JFrame {
    // Static label for karma
    private static JLabel karmaLabel;


    public UserPage(String userUsername, String theirUsername, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        // Set up the dark theme look and feel


//        database.User user = username;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            defaults.put("control", new Color(35, 35, 35));
            defaults.put("text", Color.WHITE);
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


        // Frame setup
        setTitle("User: " + theirUsername);
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(10, 10));


        // Custom Font
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);


        // Name Label
        JLabel nameLabel = new JLabel("Username: " + theirUsername, JLabel.CENTER);
        nameLabel.setFont(titleFont);
        nameLabel.setForeground(Color.WHITE);
        add(nameLabel, BorderLayout.NORTH);
        // KARTMA
//        ArrayList data = new ArrayList();
//        data.add(userUsername);
//        oos.writeObject(new Message(Message.MessageType.REMOVE_FRIEND, data));
//        oos.flush();
//        Message response = (Message) ois.readObject();
//        User user = response.getData(0);




        int karma = 0;
        // Karma Label (static)
        karmaLabel = new JLabel("Karma: " + karma, JLabel.CENTER);
        karmaLabel.setFont(buttonFont);
        karmaLabel.setForeground(Color.WHITE);
        add(karmaLabel, BorderLayout.BEFORE_FIRST_LINE);


        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        // database.User Info Label
        JLabel userInfoLabel = new JLabel("Profile of " + theirUsername, JLabel.CENTER);
        userInfoLabel.setFont(titleFont);
        userInfoLabel.setForeground(Color.WHITE);
        add(userInfoLabel, BorderLayout.CENTER);


        JPanel statusButtons = new JPanel();
        JButton statusButton = new JButton(getStatus(userUsername, theirUsername, ois, oos));
        statusButton.setFont(buttonFont);
        statusButton.addActionListener(e -> {
            try {
                String status = getStatus(userUsername,theirUsername, ois, oos);
                if (status.equals("Friended")) {


                    ArrayList data = new ArrayList();
                    data.add(userUsername);
                    data.add(theirUsername);
                    oos.writeObject(new Message(Message.MessageType.REMOVE_FRIEND, data));
                    oos.flush();
                    Message response = (Message) ois.readObject();
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Friend Removed Successfully",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        statusButton.setText("Unfriend");
                        JButton BlockButton = new JButton("Block");
                        statusButton.setFont(buttonFont);
                        statusButtons.add(statusButton);
                        statusButtons.add(BlockButton);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                this,
                                "User could not be un-friended",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }


                } else if (status.equals("Blocked")) {
                    ArrayList data = new ArrayList();
                    data.add(userUsername);
                    data.add(theirUsername);
                    oos.writeObject(new Message(Message.MessageType.UNBLOCK_USER, data));
                    oos.flush();
                    Message response = (Message) ois.readObject();
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        JOptionPane.showMessageDialog(
                                this,
                                "User unblocked successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        JButton BlockButton = new JButton("Block");
                        statusButton.setFont(buttonFont);
                        statusButtons.add(statusButton);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                this,
                                "User could not be unblocked!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else if (status.equals("hide")){
                    statusButtons.setVisible(false);
                }
                else {
                    ArrayList data = new ArrayList();
                    data.add(userUsername);
                    data.add(theirUsername);
                    oos.writeObject(new Message(Message.MessageType.ADD_FRIEND, data));
                    oos.flush();
                    Message response = (Message) ois.readObject();
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Friend added successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        statusButton.setText("Friended");
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Friend could not be added!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }




                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        statusButtons.add(statusButton);


        add(statusButtons);
        // ARGUEMENT TOPIC HISTORY
        JPanel history = getHistory(userUsername, ois, oos);
        add(history);




        // Back Button with custom rounded style
        JButton backButton = createRoundedButton("Back", new Color(70, 130, 180));
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> dispose());


        // Add padding to the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(backButton);




//        add(buttonPanel, BorderLayout.SOUTH);








        // Center the frame
        setLocationRelativeTo(null);
        setVisible(true);
    }




    public String getStatus(String username, String otherusername, ObjectInputStream ois , ObjectOutputStream oos) throws IOException {
        ArrayList data = new ArrayList();
        data.add(username);
        oos.writeObject(new Message(Message.MessageType.GET_USER, data));
        oos.flush();


        try {
            Message response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                User user = (User) response.getData().get(0);
                if (user.getFriendlist().contains(otherusername)) {
                    return "Friend";
                } else {
                    if (user.getBlockedUsers().contains(otherusername)) {
                        return "Blocked";
                    } else {
                        return "Add Friend";
                    }
                }
            }
            if (username.equals(otherusername)){
                return "hide";
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Nothing";
    }


    public JPanel getHistory(String username, ObjectInputStream ois, ObjectOutputStream oos) {
        JPanel history = new JPanel();
        // Reset the history panel
        history.removeAll();
        history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));


        try {
            // Prepare data for GET_USER request
            ArrayList data = new ArrayList();
            data.add(username);


            // Send request to get user details
            oos.writeObject(new Message(Message.MessageType.GET_USER, data));
            oos.flush();


            // Receive response
            Message response = (Message) ois.readObject();


            // Check if user retrieval was successful
            //TODO: fix lol
            if (response.getMessageType() == Message.MessageType.SUCCESS || true) {
                User user = (User) response.getData().get(0);


                // Check if user has any argument topic history
                if (user.getArgumentTopicHistory() != null) {
                    for (ArgumentTopic topic : user.getArgumentTopicHistory()) {
                        JPanel topicPanel = new JPanel(new BorderLayout());
                        topicPanel.setBackground(new Color(45, 45, 45));
                        topicPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


                        // Title as a button with wrapped text
                        JButton titleButton = createRoundedButton(topic.getTitle(), new Color(70, 130, 180));
                        titleButton.setFont(new Font("Roboto", Font.BOLD, 16));
                        titleButton.setHorizontalAlignment(SwingConstants.LEFT);
                        titleButton.addActionListener(e -> {
                            try {
                                new ArgumentTopicPage(topic, username, oos, ois);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Error opening argument topic: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        });


                        // Content as a wrapped label
                        JTextArea contentArea = new JTextArea(topic.getContent());
                        contentArea.setLineWrap(true);
                        contentArea.setWrapStyleWord(true);
                        contentArea.setEditable(false);
                        contentArea.setBackground(new Color(45, 45, 45));
                        contentArea.setForeground(Color.WHITE);
                        contentArea.setFont(new Font("Roboto", Font.PLAIN, 12));


                        // Author as a clickable label
                        JLabel authorLabel = new JLabel("By: " + topic.getAuthor().getUsername());
                        authorLabel.setForeground(new Color(120, 120, 120));
                        authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                        authorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
//                            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                                try {
//                                    new UserPage(username, topic.getAuthor().getUsername(), ois, oos);
//                                } catch (IOException e) {
//                                    JOptionPane.showMessageDialog(
//                                            UserPage.this,
//                                            "Error opening user page: " + e.getMessage(),
//                                            "Error",
//                                            JOptionPane.ERROR_MESSAGE
//                                    );
//                                } catch (ClassNotFoundException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        });


                        topicPanel.add(titleButton, BorderLayout.NORTH);
                        topicPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
                        topicPanel.add(authorLabel, BorderLayout.SOUTH);


                        history.add(topicPanel);
                        history.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between topics
                    }
                } else {
                    // Add a label if no argument topics exist
                    JLabel noHistoryLabel = new JLabel("No argument history found");
                    noHistoryLabel.setForeground(Color.WHITE);
                    noHistoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    history.add(noHistoryLabel);
                }
            } else {
                System.out.println();
                // Handle unsuccessful user retrieval
                JLabel errorLabel = new JLabel("Could not retrieve user history");
                errorLabel.setForeground(Color.RED);
                errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                history.add(errorLabel);
            }


            // Revalidate and repaint to show changes
            history.revalidate();
            history.repaint();


        } catch (IOException | ClassNotFoundException e) {
            // Comprehensive error handling
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error retrieving user history: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return history;
    }


    public void makeAT(String username, ArgumentTopic at){




    }
    // Custom rounded button method
    private JButton createRoundedButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                // Pressed state
                if (getModel().isPressed()) {
                    g2.setColor(backgroundColor.darker());
                } else {
                    g2.setColor(backgroundColor);
                }


                // Create rounded rectangle
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(
                        0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.fill(roundedRectangle);


                // Border
                g2.setColor(Color.WHITE);
                g2.draw(roundedRectangle);


                // Text
                g2.setColor(Color.WHITE);
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


    // Main method for standalone testing
}


