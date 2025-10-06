package unittests; /**
 * Team Project Phase 1 -- unittests.UserTest
 *
 * This class contains JUnit test cases to verify the functionality of the database.User class.
 * The tests cover the main methods in the database.User class, including basic operations
 * like getting and setting the username and password, handling profile photo paths,
 * equality checks, and friend list and blocked user management.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */

import database.ArgumentTopic;
import database.Comment;
import database.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.io.IOException;

public class UserTest {
    private User user;  //mock database.User
    private ArgumentTopic mockArgument; // mock database.ArgumentTopic
    private Comment mockComment;    //mockComment

    @Before
    public void setUp() throws IOException {
        user = new User("testUser", "password123");

        user.getFriendlist().clear();
        user.getArgumentTopicHistory().clear();
        user.getCommentHistory().clear();
        user.getBlockedUsers().clear();

        mockArgument = new ArgumentTopic("Sample Topic", "Sample content", user);
        mockComment = new Comment(true, "Sample comment", user.getUsername());

        user.getArgumentTopicHistory().add(mockArgument);
        user.getCommentHistory().add(mockComment);
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testProfilePhotoPathRandomness() {
        String path = user.getProfilePhotopath();   // file path of the profile photo
        assertNotNull(path);
        assertTrue(path.startsWith("data/imgs.def.pfps/"));
    }

    @Test
    public void testSetUsername() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testSetProfilePhoto() {
        user.setProfilePhoto("data/imgs/1.png");
        assertEquals("data/imgs/1.png", user.getProfilePhotopath());
    }

    @Test
    public void testGetProfilePhoto() {
        ImageIcon icon = user.getProfilePhoto();    // database.User's icon image
        assertNotNull(icon);
    }

    @Test
    public void testEquals() {
        User anotherUser = new User("testUser", "password123"); // create mock database.User object
        assertTrue(user.equals(anotherUser));

        User differentUser = new User("otherUser", "differentPassword");    // create different mock database.User object
        assertFalse(user.equals(differentUser));
    }

    @Test
    public void testToString() {
        String expected = "testUser.txt,testUser,password123," + user.getProfilePhotopath();
        assertEquals(expected, user.toString());
    }

    @Test
    public void testGetFriendList() {
        user.getFriendlist().add("friend1");
        user.getFriendlist().add("friend2");
        assertEquals(2, user.getFriendlist().size());
    }

    @Test
    public void testGetBlockedUsers() {
        user.getBlockedUsers().add("blockedUser1");
        assertEquals(1, user.getBlockedUsers().size());
    }

    @Test
    public void testGetKarma() {
        mockArgument.setEngagement(5);
        mockComment.incrementUpvotes();

        int expectedKarma = (90 * 5) + (6 * 2); // database.User's expected karma
        assertEquals(expectedKarma, user.getKarma());
    }

    @Test
    public void testAddToArgumentTopicHistory() throws IOException {
        ArgumentTopic newArgumentTopic = new ArgumentTopic("New Topic", "New content", user);
        user.addToArgumentTopicHistory(newArgumentTopic);

        // Verify that the argument topic was added
        assertEquals(2, user.getArgumentTopicHistory().size());
        assertEquals(newArgumentTopic, user.getArgumentTopicHistory().get(1)); // Ensure the last added is the new topic
    }
}
