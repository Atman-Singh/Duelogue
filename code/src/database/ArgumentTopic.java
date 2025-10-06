package database;

import java.util.ArrayList;
import java.util.UUID;
import java.io.*;
import javax.swing.*;

/**
 * Team Project Phase 1 -- database.ArgumentTopic
 *
 * This class represents argument topics on the platform.
 * ArgumentTopics contain comments, an image, and engagement metrics.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public class ArgumentTopic implements ArgumentTopicInterface, Serializable {
    // Fields
    private ArrayList<Comment> comments;    // List of comments under the database.ArgumentTopic
    private ImageIcon image;    // database.ArgumentTopic image
    private boolean updated;    // Indicates whether or not the database.ArgumentTopic has been updated
    private String title;    // database.ArgumentTopic's title
    private String content;    // database.ArgumentTopic's content
    private int engagement;    // Number of upvotes
    private User author;    // Author of database.ArgumentTopic
    private int proEngagement;    // Number of positive engagements
    private int antiEngagement;    // Number of negative engagements
    private String fileCode;    // File path of the database.ArgumentTopic
    private String imagepath;    // File path for the database.ArgumentTopic's image

    // Constructor
    public ArgumentTopic(String title, String content, User author) throws IOException {
        this.title = title;
        this.content = content;
        this.author = author;
        this.engagement = 0;
        this.updated = false;
        this.proEngagement = 0;
        this.antiEngagement = 0;
        this.comments = new ArrayList<>();
        this.fileCode = UUID.randomUUID() + ".txt";
    }

    public ArgumentTopic(String title, String content, User author, ImageIcon image) throws IOException {
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
        this.engagement = 0;
        this.updated = false;
        this.proEngagement = 0;
        this.antiEngagement = 0;
        this.comments = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        this.fileCode = uuid + ".txt";
        this.imagepath = uuid + "img.png";
    }

    public ArgumentTopic(String fileCode, String title, String content, User author) throws IOException {
        this.title = title;
        this.content = content;
        this.author = author;
        this.engagement = 0;
        this.updated = false;
        this.proEngagement = 0;
        this.antiEngagement = 0;
        this.comments = new ArrayList<>();
        this.fileCode = fileCode.substring(16);
    }

    public ArgumentTopic(String fileCode, String title, String content, User author, ImageIcon image) throws IOException {
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
        this.engagement = 0;
        this.updated = false;
        this.proEngagement = 0;
        this.antiEngagement = 0;
        this.comments = new ArrayList<>();
        this.fileCode = fileCode.substring(16);
        this.imagepath = fileCode.substring(0, fileCode.length() - 4) + "img.png";
    }

    // Getters
    public int getEngagement() {
        for (Comment comment : comments) {
            engagement += comment.getUpvoteValue();
        }
        return engagement;
    }

    /**
     * Opens a file chooser window to select an image to upload for the database.ArgumentTopic.
     * The file chooser is initially set to the "data/Posts/images" directory and only shows
     * files with the extensions "jpg", "jpeg", "png", and "gif". The selected file's path
     * is then returned. If the user cancels the file chooser, null is returned.
     * @return the path of the selected image file or null if the user canceled
     */
    public String promptForImage() {
        JFrame frame = new JFrame("Image Uploader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("data/Posts/images"));
        fileChooser.setDialogTitle("Upload an image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToUpload = fileChooser.getSelectedFile();
            String path = fileToUpload.getAbsolutePath();
            frame.dispose();
            return path;
        } else {
            frame.dispose();
            return null;
        }
    }

    public String getImagepath() {
        return imagepath;
    }

    /**
     * Iterates through all comments and adds their engagement values to the corresponding pro or anti engagement
     * field.
     */
    public void getEngagementSides() {
        for (Comment comment : comments) {
            if (comment.getSide()) {
                proEngagement += comment.getUpvoteValue();
            } else {
                antiEngagement += comment.getUpvoteValue();
            }
        }
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void appendComment(Comment comment) {
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFileCode() {
        return fileCode;
    }

    public User getAuthor() {
        return author;
    }

    public int getAntiEngagement() {
        return antiEngagement;
    }

    public int getProEngagement() {
        return proEngagement;
    }

    // Setters
    public void setEngagement(int engagement) {
        this.engagement = engagement;
    }

    // General Methods
    /**
     * Compares two ArgumentTopics for equality based on their title, content, and author.
     * @param o the object to compare
     * @return true if the two ArgumentTopics are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ArgumentTopic) {
            ArgumentTopic at = (ArgumentTopic) o;
            return title.equals(at.title) && content.equals(at.content) && author.equals(at.author);
        } else {
            return false;
        }
    }
    /**
     * Converts an database.ArgumentTopic into a string, formatted as a comma-separated list
     * of the following elements, in order:
     * <ul>
     * <li>file code</li>
     * <li>title</li>
     * <li>content</li>
     * <li>author username</li>
     * <li>engagement</li>
     * <li>imagepath</li>
     * </ul>
     * Each comment is then appended to the string, on a newline, formatted as
     * {@link Comment#toString()}.
     * @return a string representation of the database.ArgumentTopic
     */
    @Override
    public String toString() {
        String output = String.format("%s,%s,%s,%s,%d,%s\n", fileCode, title, content, author, engagement, imagepath);
        for (Comment i : comments) {
            output += i.toString() + "\n";
        }
        return output;
    }
}
