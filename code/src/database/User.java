package database; /**
 * Team Project Phase 1 -- database.User
 *
 * This class represents the platform's user.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements UserInterface, Serializable {
    private String username;    // database.User's username
    private String password;    // database.User's password
    private int karma;    // database.User's karma points
    private ArrayList<String> friendList;    // database.User's list of database.User friends
    private ArrayList<ArgumentTopic> argumentTopicHistory;    // database.User's list of previous Argument Topics
    private ArrayList<Comment> commentHistory;    // database.User's list of previous comments
    private ArrayList<String> blockedUsers;    // database.User's list of blocked Users
    private String profilePhotoPath;    // File path of database.User's profile photo

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.friendList = new ArrayList<>();
        this.argumentTopicHistory = new ArrayList<>();
        this.commentHistory = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();

        String[] profilePhotoPaths = new String[]{
                "data/imgs.def.pfps/1.png", "data/imgs.def.pfps/2.png",
                "data/imgs.def.pfps/3.png", "data/imgs.def.pfps/4.png", "data/imgs.def.pfps/5.png"
        };
        int randomIndex = (int) (Math.random() * profilePhotoPaths.length);
        profilePhotoPath = profilePhotoPaths[randomIndex];
    }

    // Getters
    public ArrayList<ArgumentTopic> getArgumentTopicHistory() {
        return argumentTopicHistory;
    }

    @Override
    public ArrayList<String> getFriendlist() {
        return friendList;
    }
    
    public void addToArgumentTopicHistory(ArgumentTopic argumentTopic) {
        argumentTopicHistory.add(argumentTopic);
    }

    /**
     * Calculates and returns the user's karma points.
     *
     * The karma is computed as a weighted sum of engagement from the user's
     * argument topics and comments. Each argument topic's engagement is
     * multiplied by 90, and each comment's upvote value is multiplied by 6.
     *
     * @return the total calculated karma for the user
     */
    public int getKarma() {
        int calculatedKarma = 0; // Reset karma calculation for this call

        // Calculate karma from argument topics
        for (ArgumentTopic argumentTopic : argumentTopicHistory) {
            calculatedKarma += 90 * argumentTopic.getEngagement();
        }

        // Calculate karma from comments
        for (Comment comment : commentHistory) {
            calculatedKarma += 6 * comment.getUpvoteValue();
        }

        karma = calculatedKarma;
        return calculatedKarma;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getBlockedUsers() {
        return blockedUsers;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePhoto(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getProfilePhotopath() {
        return this.profilePhotoPath;
    }

    public ImageIcon getProfilePhoto() {
        return new ImageIcon(profilePhotoPath);
    }

    public ArrayList<Comment> getCommentHistory() {
        return commentHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;  // Cast to database.User for comparison
            return username.equals(u.username) && password.equals(u.password);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        // Format: username.txt, username, password, profile photo path
        return username + ".txt," + username + "," + password + "," + profilePhotoPath;
    }
}
