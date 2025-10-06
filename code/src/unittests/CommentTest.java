package unittests;

import database.Comment;
import database.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Team Project Phase 1 -- unittests.CommentTest.java
 *
 * This class contains JUnit test cases to verify the functionality of the database.Comment class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public class CommentTest {

    private Comment comment;
    private User mockUser;

    @Before
    public void setUp() {
        mockUser = new User("testUser", "password123");
        comment = new Comment(true, "This is a sample comment.", mockUser.getUsername());
    }

    @Test
    public void testGetUpvotesInitial() {
        // Initially, the comment should have 1 upvote
        assertEquals(1, comment.getUpvoteValue());
    }

    @Test
    public void testIncrementUpvotes() {
        comment.incrementUpvotes();
        assertEquals(2, comment.getUpvoteValue());
    }

    @Test
    public void testIncrementDownvotes() {
        comment.incrementDownvotes();
        assertEquals(0, comment.getUpvoteValue()); // 1 upvote - 1 downvote = 0
    }

    @Test
    public void testGetSide() {
        assertTrue(comment.getSide());
    }

    @Test
    public void testGetContent() {
        assertEquals("This is a sample comment.", comment.getContent());
    }

    @Test
    public void testEquals() {
        Comment anotherComment = new Comment(true, "This is a sample comment.", mockUser.getUsername());
        assertTrue(comment.equals(anotherComment));

        Comment differentComment = new Comment(false, "Different content.", new User("otherUser",
                "pass").getUsername());
        assertFalse(comment.equals(differentComment));
    }

    @Test
    public void testToString() {
        String expectedString = String.format("%s,%s,%b",
                comment.getContent(),
                mockUser.getUsername(),
                comment.getSide());
        assertEquals(expectedString, comment.toString());
    }
}
