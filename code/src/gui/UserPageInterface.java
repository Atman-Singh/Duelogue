package gui;

import java.io.IOException;


/**
 * Team Project Phase 3 -- UserPageInterface
 * This interface outlines the methods that should be implemented for any UserPage object
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */


public interface UserPageInterface {
    String getStatus(String username, String otherUsername) throws IOException; // Retrieve the friendship status
    void populateHistory(String username); // Populate user argument history
    void logout(); // Handle user logout or page exit
}
