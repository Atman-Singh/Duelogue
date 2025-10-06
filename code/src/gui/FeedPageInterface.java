package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Team Project Phase 3 -- FeedPageInterface
 * This interface outlines the methods that should be implemented for any FeedPage object
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

public interface FeedPageInterface {
    void populateFeed(); // Populate the news feed panel
    void showAddFriendPanel(String username); // Show the add friend panel
    void logout(); // Handle user logout
}
