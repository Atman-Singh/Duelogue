package unittests;

import database.ArgumentTopic;
import database.Comment;
import database.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Team Project Phase 1 -- unittests.ArgumentTopicTest
 *
 * This class contains JUnit test cases to verify the functionality of the database.ArgumentTopic class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public class ArgumentTopicTest {

    private ArgumentTopic argumentTopic;
    private User mockUser;
    private Comment mockComment1;
    private Comment mockComment2;

    @Before
    public void setUp() throws IOException {
        mockUser = new User("testUser", "password123");
        argumentTopic = new ArgumentTopic("Sample Title", "Sample Content", mockUser);

        // Initialize mock comments
        mockComment1 = new Comment(true, "This is a pro comment.", mockUser.getUsername());
        mockComment2 = new Comment(false, "This is an anti comment.", mockUser.getUsername());

        // Set up initial upvotes for comments
        mockComment1.incrementUpvotes(); // Upvotes = 2 (1 initial + 1 increment)
        mockComment2.incrementUpvotes(); // Upvotes = 2 (1 initial + 1 increment)
    }

    @Test
    public void testGetTitle() {
        assertEquals("Sample Title", argumentTopic.getTitle());
    }

    @Test
    public void testGetContent() {
        assertEquals("Sample Content", argumentTopic.getContent());
    }

    @Test
    public void testGetAuthor() {
        assertEquals(mockUser, argumentTopic.getAuthor());
    }

    @Test
    public void testGetFileCode() {
        assertNotNull(argumentTopic.getFileCode());
        assertTrue(argumentTopic.getFileCode().endsWith(".txt"));
    }

    @Test
    public void testAppendComment() {
        argumentTopic.appendComment(mockComment1);
        assertEquals(1, argumentTopic.getComments().size());
        assertEquals(mockComment1, argumentTopic.getComments().get(0));
    }

    @Test
    public void testGetEngagement() {
        argumentTopic.appendComment(mockComment1);
        argumentTopic.appendComment(mockComment2);
        int expectedEngagement = mockComment1.getUpvoteValue()+ mockComment2.getUpvoteValue();
        assertEquals(expectedEngagement, argumentTopic.getEngagement());
    }

    @Test
    public void testGetEngagementSides() {
        argumentTopic.appendComment(mockComment1);
        argumentTopic.appendComment(mockComment2);
        argumentTopic.getEngagementSides();

        assertEquals(2, argumentTopic.getProEngagement());
        assertEquals(2, argumentTopic.getAntiEngagement());
    }

    @Test
    public void testEquals() throws IOException {
        ArgumentTopic anotherArgumentTopic = new ArgumentTopic("Sample Title", "Sample Content", mockUser);
        assertTrue(argumentTopic.equals(anotherArgumentTopic));

        ArgumentTopic differentArgumentTopic = new ArgumentTopic("Different Title", "Different Content",
                new User("otherUser", "pass"));
        assertFalse(argumentTopic.equals(differentArgumentTopic));
    }

    @Test
    public void testToString() {
        argumentTopic.appendComment(mockComment1);
        argumentTopic.appendComment(mockComment2);

        String expected = String.format("%s,%s,%s,%s,%s,%s\n",
                argumentTopic.getFileCode(),
                argumentTopic.getTitle(),
                argumentTopic.getContent(),
                argumentTopic.getAuthor(),
                argumentTopic.getEngagement(),
                argumentTopic.getImagepath());

        expected += mockComment1.toString() + "\n" + mockComment2.toString() + "\n";

        assertEquals(expected, argumentTopic.toString());
    }
}
