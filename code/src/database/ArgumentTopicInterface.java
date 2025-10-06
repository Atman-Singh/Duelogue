package database;

import java.util.ArrayList;

/**
 * Team Project Phase 1 -- database.ArgumentTopicInterface
 *
 * This Interface outlines the general functionality of the database.ArgumentTopic class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public interface ArgumentTopicInterface {
    // Getters
    int getEngagement();
    String getImagepath();
    ArrayList<Comment> getComments();
    String getTitle();
    String getContent();
    String getFileCode();
    User getAuthor();

    // General Methods
    void getEngagementSides();
    void appendComment(Comment comment);

    // Override Methods
    boolean equals(Object o);
    String toString();
}
