package gui;

import database.ArgumentTopic;
import database.Comment;
import networkio.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Team Project Phase 3 -- ArgumentTopicPage
 * Page displayed when viewing a post
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

class ArgumentTopicPage extends JFrame {
    public static String at;
    JTextArea proCommentBox;
    JTextArea antiCommentBox;
    ObjectOutputStream oos;
    ObjectInputStream ois;



    // Dark theme colors
    private static final Color DARK_BACKGROUND = new Color(30, 30, 30);
    private static final Color DARK_PANEL_BACKGROUND = new Color(0, 0, 0);
    private static final Color DARK_TEXT = Color.WHITE;
    private static final Color DARK_BORDER = new Color(163, 163, 163);

    public ArgumentTopicPage(ArgumentTopic argumentTopic, String username, ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        int totalUpvotes = 0;
        int totalDownvotes = 0;
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);


        // Set dark theme for the entire frame
        getContentPane().setBackground(DARK_BACKGROUND);
        setTitle("Argument Topic: " + argumentTopic.getTitle());
        setSize(1200, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title label with dark theme
        JLabel titleLabel = new JLabel(argumentTopic.getTitle(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(DARK_TEXT);
        add(titleLabel, BorderLayout.NORTH);

        // Main content area with dark theme
        JTextArea contentArea = new JTextArea(argumentTopic.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setEditable(false);
        contentArea.setBackground(DARK_PANEL_BACKGROUND);
        contentArea.setForeground(DARK_TEXT);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(contentScrollPane, BorderLayout.CENTER);

        // Panels for Pros and Cons with dark theme
        JPanel commentsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        commentsPanel.setBackground(DARK_BACKGROUND);

        JPanel prosPanel = new JPanel();
        prosPanel.setLayout(new BoxLayout(prosPanel, BoxLayout.Y_AXIS));
        prosPanel.setBorder(BorderFactory.createTitledBorder("Pros"));
        prosPanel.setBackground(DARK_BACKGROUND);

        JPanel consPanel = new JPanel();
        consPanel.setLayout(new BoxLayout(consPanel, BoxLayout.Y_AXIS));
        consPanel.setBorder(BorderFactory.createTitledBorder("Cons"));
        consPanel.setBackground(DARK_BACKGROUND);

        // database.Comment input areas with dark theme
        proCommentBox = new JTextArea("Enter a supporting comment here", 3, 20);
        proCommentBox.setLineWrap(true);
        proCommentBox.setWrapStyleWord(true);
        antiCommentBox = new JTextArea("Enter an opposing comment here", 3, 20);
        antiCommentBox.setLineWrap(true);
        antiCommentBox.setWrapStyleWord(true);
        // Dark theme for comment boxes
        proCommentBox.setBackground(DARK_PANEL_BACKGROUND);
        proCommentBox.setForeground(DARK_TEXT);
        antiCommentBox.setBackground(DARK_PANEL_BACKGROUND);
        antiCommentBox.setForeground(DARK_TEXT);

        JButton postSupportingCommentButton = new JButton("Comment for");
        JButton postAntiCommentButton = new JButton("Comment against");

        // Dark theme for buttons
        postSupportingCommentButton.setBackground(DARK_PANEL_BACKGROUND);
        postSupportingCommentButton.setForeground(DARK_TEXT);
        postAntiCommentButton.setBackground(DARK_PANEL_BACKGROUND);
        postAntiCommentButton.setForeground(DARK_TEXT);

        // Comments display and interaction
        ArrayList<Comment> comments = argumentTopic.getComments();
        if (comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
                JPanel singleCommentPanel = new JPanel();
                singleCommentPanel.setLayout(new BoxLayout(singleCommentPanel, BoxLayout.Y_AXIS));
                singleCommentPanel.setBackground(DARK_PANEL_BACKGROUND);
                singleCommentPanel.setBorder(BorderFactory.createLineBorder(DARK_BORDER, 1, true));
                singleCommentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Limit comment height

                // database.Comment content
                JTextArea commentContentArea = new JTextArea(comment.getContent());
                commentContentArea.setLineWrap(true);
                commentContentArea.setWrapStyleWord(true);
                commentContentArea.setEditable(false);
                commentContentArea.setBackground(DARK_PANEL_BACKGROUND);
                commentContentArea.setForeground(DARK_TEXT);

                // Author label with clickable functionality
                JLabel authorLabel = new JLabel(comment.getAuthor());
                authorLabel.setForeground(Color.WHITE);
                authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                authorLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        // Add action for clicking on author label
                        // For example, show user profile or send message
                        try {
                            new UserPage(username, comment.getAuthor(), ois, oos);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                // Voting panel
                JPanel votingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                votingPanel.setBackground(DARK_PANEL_BACKGROUND);

                // Resize icons (assuming original icons were large)
                ImageIcon upvoteIcon = new ImageIcon(new ImageIcon("code/data/assets/upvote.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                ImageIcon downvoteIcon = new ImageIcon(new ImageIcon("code/data/assets/downvote.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                ImageIcon deleteIcon = new ImageIcon(new ImageIcon("code/data/assets/trashcan.png").getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));
                JButton upvoteButton = new JButton(upvoteIcon);
                JButton downvoteButton = new JButton(downvoteIcon);
                JButton deleteButton = new JButton(deleteIcon);




                deleteButton.addActionListener(e -> {
                    Message response = deleteComment(argumentTopic, comment.getCommentCode());
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        Container parentPanel = singleCommentPanel.getParent();
                        if (parentPanel != null) {
                            parentPanel.remove(singleCommentPanel);
                            parentPanel.revalidate(); // Recalculate layout
                            parentPanel.repaint();   // Redraw the panel
                        }
                        ArgumentTopic at = (ArgumentTopic) response.getData().get(0);
                        new ArgumentTopicPage(at, username, oos, ois);
                    }
                });

                // Dark theme for voting buttons
                upvoteButton.setBackground(DARK_PANEL_BACKGROUND);
                downvoteButton.setBackground(DARK_PANEL_BACKGROUND);

                JLabel upvoteLabel = new JLabel("Upvotes: " + comment.getUpvotes());
                JLabel downvoteLabel = new JLabel("Downvotes: " + comment.getDownvotes());

                // Dark theme for labels
                upvoteLabel.setForeground(DARK_TEXT);
                downvoteLabel.setForeground(DARK_TEXT);

                votingPanel.add(upvoteButton);
                votingPanel.add(upvoteLabel);
                votingPanel.add(downvoteButton);
                votingPanel.add(downvoteLabel);

                upvoteButton.addActionListener(e -> {
                    Message response = upVoteComment(comment, username, argumentTopic);
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        upvoteLabel.setText("Upvotes: " + (Integer) response.getData().get(0));
                    }
                });
                downvoteButton.addActionListener(e -> {
                    Message response = downVoteComment(comment, username, argumentTopic);
                    if (response.getMessageType() == Message.MessageType.SUCCESS) {
                        downvoteLabel.setText("Downvotes: " + (Integer) response.getData().get(0));
                    }

                });

                if (argumentTopic.getAuthor().getUsername().equals(username) ||  comment.getAuthor().equals(username)) {
                    votingPanel.add(deleteButton);
                }

                // Scroll pane for comment content to prevent horizontal scrolling
                JScrollPane commentScrollPane = new JScrollPane(commentContentArea);
                commentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                singleCommentPanel.add(commentScrollPane);
                singleCommentPanel.add(authorLabel);
                singleCommentPanel.add(votingPanel);

                // Add to appropriate panel based on side
                if (comment.getSide()) {
                    prosPanel.add(singleCommentPanel);
                    prosPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between comments
                    totalUpvotes += comment.getUpvotes();
                } else {
                    consPanel.add(singleCommentPanel);
                    consPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between comments
                    totalDownvotes += comment.getDownvotes();
                }
            }
        }

        // Overall stats panel with dark theme
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statsPanel.setBackground(DARK_BACKGROUND);
        JLabel totalUpvotesLabel = new JLabel("Total Upvotes: " + totalUpvotes);
        JLabel totalDownvotesLabel = new JLabel("Total Downvotes: " + totalDownvotes);
        totalUpvotesLabel.setForeground(DARK_TEXT);
        totalDownvotesLabel.setForeground(DARK_TEXT);
        statsPanel.add(totalUpvotesLabel);
        statsPanel.add(totalDownvotesLabel);

        // database.Comment input and posting with dark theme
        JPanel commentInputPanel = new JPanel(new GridLayout(1, 2));
        commentInputPanel.setBackground(DARK_BACKGROUND);

        // Scroll panes with no horizontal scrolling
        JScrollPane proScrollPane = new JScrollPane(proCommentBox);
        proScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        proScrollPane.setBackground(DARK_BACKGROUND);

        JScrollPane antiScrollPane = new JScrollPane(antiCommentBox);
        antiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        antiScrollPane.setBackground(DARK_BACKGROUND);

        JPanel proInputPanel = new JPanel(new BorderLayout());
        proInputPanel.setBackground(DARK_BACKGROUND);
        proInputPanel.add(proScrollPane, BorderLayout.CENTER);
        proInputPanel.add(postSupportingCommentButton, BorderLayout.SOUTH);

        JPanel antiInputPanel = new JPanel(new BorderLayout());
        antiInputPanel.setBackground(DARK_BACKGROUND);
        antiInputPanel.add(antiScrollPane, BorderLayout.CENTER);
        antiInputPanel.add(postAntiCommentButton, BorderLayout.SOUTH);
        commentInputPanel.add(proInputPanel);
        commentInputPanel.add(antiInputPanel);

        // Add comment posting event listeners
        postSupportingCommentButton.addActionListener(e -> {
            Message response = postComment(argumentTopic, true, proCommentBox.getText(), username);
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                ArgumentTopic at = (ArgumentTopic) response.getData().get(0);
                dispose();
                new ArgumentTopicPage(at, username, oos, ois, (Comment) response.getData().get(1));
            }
        });

        postAntiCommentButton.addActionListener(e -> {
            Message response = postComment(argumentTopic, true, antiCommentBox.getText(), username);
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                ArgumentTopic at = (ArgumentTopic) response.getData().get(0);
                dispose();
                new ArgumentTopicPage(at, username, oos, ois, (Comment) response.getData().get(1));
            }

        });

        // Layout for comments and input with dark theme
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(DARK_BACKGROUND);
        JPanel commentDisplayPanel = new JPanel(new GridLayout(1, 2));
        commentDisplayPanel.setBackground(DARK_BACKGROUND);

        // Scroll panes to prevent horizontal scrolling
        JScrollPane proCommentsScrollPane = new JScrollPane(prosPanel);
        proCommentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane consCommentsScrollPane = new JScrollPane(consPanel);
        consCommentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        commentDisplayPanel.add(proCommentsScrollPane);
        commentDisplayPanel.add(consCommentsScrollPane);

        bottomPanel.add(statsPanel, BorderLayout.NORTH);
        bottomPanel.add(commentDisplayPanel, BorderLayout.CENTER);
        bottomPanel.add(commentInputPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public Message deleteComment(ArgumentTopic topic, String commentCode) {
        Message response = new Message(Message.MessageType.ERROR, "");
        try {
            ArrayList data = new ArrayList();
            data.add(topic.getFileCode());
            data.add(commentCode);
            oos.writeObject(new Message(Message.MessageType.DELETE_COMMENT, data));
            oos.flush();

            response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                JLabel messageLabel = new JLabel("Comment deleted successfully!");
                messageLabel.setForeground(Color.WHITE);
                JOptionPane.showMessageDialog(this, messageLabel);
                return response;
            } else {
                JOptionPane.showMessageDialog(this, "Error deelting comment");
                return response;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error posting comment");
        }
        return response;
    }

    public ArgumentTopicPage(ArgumentTopic argumentTopic, String username, ObjectOutputStream oos,
                             ObjectInputStream ois, Comment newComment) {
        this.oos = oos;
        this.ois = ois;
        int totalUpvotes = 0;
        int totalDownvotes = 0;
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);

        // Set dark theme for the entire frame
        getContentPane().setBackground(DARK_BACKGROUND);
        setTitle("Argument Topic: " + argumentTopic.getTitle());
        setSize(1200, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title label with dark theme
        JLabel titleLabel = new JLabel(argumentTopic.getTitle(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(DARK_TEXT);
        add(titleLabel, BorderLayout.NORTH);

        // Main content area with dark theme
        JTextArea contentArea = new JTextArea(argumentTopic.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setEditable(false);
        contentArea.setBackground(DARK_PANEL_BACKGROUND);
        contentArea.setForeground(DARK_TEXT);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(contentScrollPane, BorderLayout.CENTER);

        // Panels for Pros and Cons with dark theme
        JPanel commentsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        commentsPanel.setBackground(DARK_BACKGROUND);

        JPanel prosPanel = new JPanel();
        prosPanel.setLayout(new BoxLayout(prosPanel, BoxLayout.Y_AXIS));
        prosPanel.setBorder(BorderFactory.createTitledBorder("Pros"));
        prosPanel.setBackground(DARK_BACKGROUND);

        JPanel consPanel = new JPanel();
        consPanel.setLayout(new BoxLayout(consPanel, BoxLayout.Y_AXIS));
        consPanel.setBorder(BorderFactory.createTitledBorder("Cons"));
        consPanel.setBackground(DARK_BACKGROUND);

        // database.Comment input areas with dark theme
        proCommentBox = new JTextArea("Enter a supporting comment here", 3, 20);
        proCommentBox.setLineWrap(true);
        proCommentBox.setWrapStyleWord(true);
        antiCommentBox = new JTextArea("Enter an opposing comment here", 3, 20);
        antiCommentBox.setLineWrap(true);
        antiCommentBox.setWrapStyleWord(true);
        // Dark theme for comment boxes
        proCommentBox.setBackground(DARK_PANEL_BACKGROUND);
        proCommentBox.setForeground(DARK_TEXT);
        antiCommentBox.setBackground(DARK_PANEL_BACKGROUND);
        antiCommentBox.setForeground(DARK_TEXT);

        JButton postSupportingCommentButton = new JButton("Comment for");
        JButton postAntiCommentButton = new JButton("Comment against");

        // Dark theme for buttons
        postSupportingCommentButton.setBackground(DARK_PANEL_BACKGROUND);
        postSupportingCommentButton.setForeground(DARK_TEXT);
        postAntiCommentButton.setBackground(DARK_PANEL_BACKGROUND);
        postAntiCommentButton.setForeground(DARK_TEXT);

        // Comments display and interaction
        ArrayList<Comment> comments = argumentTopic.getComments();
//        comments.add(0,new Comment(true, "This is a discussion.", username, argumentTopic.getFileCode(), 1,0));
        comments.add(newComment);
        for (Comment comment : comments) {
            JPanel singleCommentPanel = new JPanel();
            singleCommentPanel.setLayout(new BoxLayout(singleCommentPanel, BoxLayout.Y_AXIS));
            singleCommentPanel.setBackground(DARK_PANEL_BACKGROUND);
            singleCommentPanel.setBorder(BorderFactory.createLineBorder(DARK_BORDER, 1, true));
            singleCommentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Limit comment height

            // database.Comment content
            JTextArea commentContentArea = new JTextArea(comment.getContent());
            commentContentArea.setLineWrap(true);
            commentContentArea.setWrapStyleWord(true);
            commentContentArea.setEditable(false);
            commentContentArea.setBackground(DARK_PANEL_BACKGROUND);
            commentContentArea.setForeground(DARK_TEXT);

            // Author label with clickable functionality
            JLabel authorLabel = new JLabel(comment.getAuthor());
            authorLabel.setForeground(Color.WHITE);
            authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            authorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Add action for clicking on author label
                    // For example, show user profile or send message
                    try {
                        new UserPage(username, comment.getAuthor(), ois, oos);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Voting panel
            JPanel votingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            votingPanel.setBackground(DARK_PANEL_BACKGROUND);

            // Resize icons (assuming original icons were large)
            ImageIcon upvoteIcon = new ImageIcon(new ImageIcon("code/data/assets/upvote.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            ImageIcon downvoteIcon = new ImageIcon(new ImageIcon("code/data/assets/downvote.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

            JButton upvoteButton = new JButton(upvoteIcon);
            JButton downvoteButton = new JButton(downvoteIcon);


            //old implementation
//            upvoteButton.addActionListener(e -> {
//                ArrayList<Object> data = new ArrayList<>();
//                data.add(comment);
//                try {
//                    oos.writeObject(new Message(Message.MessageType.UPVOTE, data));
//                    oos.flush();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            });
//            downvoteButton.addActionListener(e -> {
//                ArrayList<Object> data = new ArrayList<>();
//                data.add(comment);
//                try {
//                    oos.writeObject(new Message(Message.MessageType.DOWNVOTE, data));
//                    oos.flush();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            });

            // Dark theme for voting buttons
            upvoteButton.setBackground(DARK_PANEL_BACKGROUND);
            downvoteButton.setBackground(DARK_PANEL_BACKGROUND);

            JLabel upvoteLabel = new JLabel("Upvotes: " + comment.getUpvotes());
            JLabel downvoteLabel = new JLabel("Downvotes: " + comment.getDownvotes());

            // Dark theme for labels
            upvoteLabel.setForeground(DARK_TEXT);
            downvoteLabel.setForeground(DARK_TEXT);

            votingPanel.add(upvoteButton);
            votingPanel.add(upvoteLabel);
            votingPanel.add(downvoteButton);
            votingPanel.add(downvoteLabel);

            upvoteButton.addActionListener(e -> {
            });
            // Scroll pane for comment content to prevent horizontal scrolling
            JScrollPane commentScrollPane = new JScrollPane(commentContentArea);
            commentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            singleCommentPanel.add(commentScrollPane);
            singleCommentPanel.add(authorLabel);
            singleCommentPanel.add(votingPanel);

            // Add to appropriate panel based on side
            if (comment.getSide()) {
                prosPanel.add(singleCommentPanel);
                prosPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between comments
                totalUpvotes += comment.getUpvotes();
            } else {
                consPanel.add(singleCommentPanel);
                consPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between comments
                totalDownvotes += comment.getDownvotes();
            }
        }
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setBackground(DARK_BACKGROUND); // Match the dark theme
        JButton backButton = new JButton("Back to Feed");
        backButton.setBackground(DARK_PANEL_BACKGROUND);
        backButton.setForeground(DARK_TEXT);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            dispose();
            new FeedPage(username, oos, ois);
        });

        backButtonPanel.add(backButton);
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statsPanel.add(backButtonPanel);


        // Overall stats panel with dark theme

        statsPanel.setBackground(DARK_BACKGROUND);
        JLabel totalUpvotesLabel = new JLabel("Total Upvotes: " + totalUpvotes);
        JLabel totalDownvotesLabel = new JLabel("Total Downvotes: " + totalDownvotes);
        totalUpvotesLabel.setForeground(DARK_TEXT);
        totalDownvotesLabel.setForeground(DARK_TEXT);
        statsPanel.add(totalUpvotesLabel);
        statsPanel.add(totalDownvotesLabel);
        // database.Comment input and posting with dark theme
        JPanel commentInputPanel = new JPanel(new GridLayout(1, 2));
        commentInputPanel.setBackground(DARK_BACKGROUND);

        // Scroll panes with no horizontal scrolling
        JScrollPane proScrollPane = new JScrollPane(proCommentBox);
        proScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        proScrollPane.setBackground(DARK_BACKGROUND);

        JScrollPane antiScrollPane = new JScrollPane(antiCommentBox);
        antiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        antiScrollPane.setBackground(DARK_BACKGROUND);

        JPanel proInputPanel = new JPanel(new BorderLayout());
        proInputPanel.setBackground(DARK_BACKGROUND);
        proInputPanel.add(proScrollPane, BorderLayout.CENTER);
        proInputPanel.add(postSupportingCommentButton, BorderLayout.SOUTH);

        JPanel antiInputPanel = new JPanel(new BorderLayout());
        antiInputPanel.setBackground(DARK_BACKGROUND);
        antiInputPanel.add(antiScrollPane, BorderLayout.CENTER);
        antiInputPanel.add(postAntiCommentButton, BorderLayout.SOUTH);

        commentInputPanel.add(proInputPanel);
        commentInputPanel.add(antiInputPanel);

        postSupportingCommentButton.addActionListener(e -> {
            Message response = postComment(argumentTopic, true, proCommentBox.getText(), username);
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                dispose();
                new FeedPage(username, oos, ois);
                ArgumentTopic at = (ArgumentTopic) response.getData().get(0);
                new ArgumentTopicPage(at, username, oos, ois, (Comment) response.getData().get(1));
            }
        });

        postAntiCommentButton.addActionListener(e -> {
            Message response = postComment(argumentTopic, false, antiCommentBox.getText(), username);
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                dispose();
                ArgumentTopic at = (ArgumentTopic) response.getData().get(0);
                new ArgumentTopicPage(at, username, oos, ois, (Comment) response.getData().get(1));
            }
        });

        // Layout for comments and input with dark theme
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setVisible(true);
        bottomPanel.setBackground(DARK_BACKGROUND);
        JPanel commentDisplayPanel = new JPanel(new GridLayout(1, 2));
        commentDisplayPanel.setBackground(DARK_BACKGROUND);

        // Scroll panes to prevent horizontal scrolling
        JScrollPane proCommentsScrollPane = new JScrollPane(prosPanel);
        proCommentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane consCommentsScrollPane = new JScrollPane(consPanel);
        consCommentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        commentDisplayPanel.add(proCommentsScrollPane);
        commentDisplayPanel.add(consCommentsScrollPane);

        bottomPanel.add(statsPanel, BorderLayout.NORTH);

        bottomPanel.add(commentDisplayPanel, BorderLayout.CENTER);
        bottomPanel.add(commentInputPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
        add(statsPanel);
    }


    public Message upVoteComment(Comment comment, String user, ArgumentTopic topic) {
        Message response = new Message(Message.MessageType.ERROR, "");
        try {
            ArrayList data = new ArrayList<>();
            data.add(topic.getFileCode());
            data.add(comment.getCommentCode());
            data.add(user);
            oos.writeObject(new Message(Message.MessageType.UPVOTE, data));
            oos.flush();

            response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                JLabel messageLabel = new JLabel("Comment upvoted successfully!");
                messageLabel.setForeground(Color.WHITE);
                JOptionPane.showMessageDialog(this, messageLabel);

            }

            else {
                JOptionPane.showMessageDialog(this, "Error upvoting comment");
                return response;
            }


        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error upvoting comment");
        }
        return response;
    }

    public Message downVoteComment(Comment comment, String user, ArgumentTopic topic) {
        Message response = new Message(Message.MessageType.ERROR, "");
        try {
            ArrayList data = new ArrayList<>();
            data.add(topic.getFileCode());
            data.add(comment.getCommentCode());
            data.add(user);
            oos.writeObject(new Message(Message.MessageType.DOWNVOTE, data));
            oos.flush();

            response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                JLabel messageLabel = new JLabel("Comment downvoted successfully!");
                messageLabel.setForeground(Color.WHITE);
                JOptionPane.showMessageDialog(this, messageLabel);

            }

            else {
                JOptionPane.showMessageDialog(this, "Error downvoting comment");
                return response;
            }


        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error upvoting comment");
        }
        return response;
    }



        public Message postComment(ArgumentTopic topic, boolean side, String content, String author) {
        if (content == null || content.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter content.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        Message response = new Message(Message.MessageType.ERROR, "");
        try {
            ArrayList data = new ArrayList();
            data.add(topic.getFileCode());
            data.add(content);
            data.add(author);
            data.add(side);

            oos.writeObject(new Message(Message.MessageType.ADD_COMMENT, data));
            oos.flush();

            response = (Message) ois.readObject();
            if (response.getMessageType() == Message.MessageType.SUCCESS) {
                System.out.println("Recieved: " +
                        response.getData().get(1));
                JLabel messageLabel = new JLabel("Comment posted successfully!");
                messageLabel.setForeground(Color.WHITE);
                JOptionPane.showMessageDialog(this, messageLabel);

                // Clear the comment box after successful posting
                if (side) {
                    proCommentBox.setText("");
                } else {
                    antiCommentBox.setText("");
                }
                return response;
            } else {
                JOptionPane.showMessageDialog(this, "Error posting comment");
                return response;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error posting comment");
        }
        return response;
    }
}