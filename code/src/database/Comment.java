package database; /**
 * Team Project Phase 1 -- database.Comment
 *
 * This class represents a comment under an database.ArgumentTopic post.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Comment implements CommentInterface, Serializable {
    // Fields
    private boolean side;   // indicates whether the database.Comment is for or against the database.ArgumentTopic
    private int upvotes;    // number of upvotes
    private int downvotes;  // number of downvotes
    private String content; // text content of the database.Comment
    private String author;    // author of the database.Comment
    private String commentCode;

    // Constructor
    public Comment(boolean side, String content, String author) {
        this.side = side;
        this.content = content;
        this.author = author;
        this.upvotes = 1;
        this.downvotes = 0;
        this.commentCode = UUID.randomUUID() + "";
    }

    public Comment(boolean side, String content, String author, String uuid, int upvotes, int downvotes) {
        this.side = side;
        this.content = content;
        this.author = author;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.commentCode = uuid;
    }

    // Getters
    public int getUpvoteValue() {
        return upvotes - downvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }

    public boolean getSide() {
        return side;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getCommentCode() {
        return commentCode;
    }


    public boolean equals(Object o) {
        if (o instanceof Comment) {
            Comment com = (Comment) o;
            return side == com.side && content.equals(com.content) && author.equals(com.author);
        } else {
            return false;
        }
    }

    /**
     * Converts a database.Comment into a string, formatted as a comma-separated list
     * of the following elements, in order:
     * <ul>
     * <li>content</li>
     * <li>author's username</li>
     * <li>side, as a boolean</li>
     * </ul>
     * @return a string representation of the database.Comment
     */
    public String toString() {
        return String.format("%s,%s,%b", content, author, side);
    }
}
