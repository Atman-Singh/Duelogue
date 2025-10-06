package database; /**
 * Team Project Phase 1 -- database.UserInterface
 *
 * This Interface outlines the general functionality of the database.User class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

import javax.swing.*;
import java.util.ArrayList;

public interface UserInterface {
    // Getters
    ArrayList<ArgumentTopic> getArgumentTopicHistory();
    ArrayList<String> getFriendlist();
    int getKarma();
    String getUsername();
    ArrayList<String> getBlockedUsers();
    String getPassword();
    String getProfilePhotopath();
    ImageIcon getProfilePhoto();

    // Setters
    void setUsername(String username);
    void setProfilePhoto(String profilePhotoPath);

    // Override Methods
    boolean equals(Object o);
    String toString();
}
