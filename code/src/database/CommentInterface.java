package database;/* Team Project Phase 1 -- database.CommentInterface
 *
 * This Interface outlines the general functionality of the database.Comment class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public interface CommentInterface {
    // Getters
    int getUpvoteValue();
    int getUpvotes();
    int getDownvotes();
    boolean getSide();
    String getContent();
    String getAuthor();

    // Voting Methods
    void incrementUpvotes();
    void incrementDownvotes();

    // Override Methods
    boolean equals(Object o);
    String toString();
}
