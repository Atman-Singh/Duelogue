package unittests;

import database.ArgumentTopic;
import database.Comment;
import database.Database;
import database.User;
import exceptions.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;
import java.io.File;

/**
 * Team Project Phase 1 -- unittests.DatabaseTest
 *
 * This class contains JUnit test cases to verify the functionality of the database.Database class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */

public class DatabaseTest {
    private Database database;
    private Database database2;
    private ArrayList<String> userPaths;

    @Before
    public void setUp() {
        userPaths = new ArrayList<>();
        userPaths.add("User1_Username.txt");
        userPaths.add("User2_Username.txt");
        userPaths.add("User3_Username.txt");

        database = new Database();
        database2 = new Database();
    }

    @Test
    public void testCreateUser() throws InvalidCredentialsException, InvalidUserException {
        // Test valid user creation
        boolean result = database.createUser("testUser1", "Test@123");
        assertTrue("Valid user creation should return true", result);

        // Test duplicate username
        try {
            database.createUser("testUser1", "Different@123");
            fail("Should throw Exceptions.InvalidCredentialsException for duplicate username");
        } catch (InvalidCredentialsException e) {
            assertEquals("This username is already taken", e.getMessage());
        }
        // Test invalid password

        try {
            database.createUser("testUser2", "weak");
            fail("Should throw Exceptions.InvalidCredentialsException for weak password");
        } catch (InvalidCredentialsException e) {
            assertTrue(e.getMessage().contains("Password must be at least 8 characters long"));
        }
    }

    @Test
    public void testCreateArgumentTopic() throws InvalidCredentialsException, IOException, InvalidUserException {
        // Create a user first
        database.createUser("testUser", "Test@123");

        // Test valid argument topic creation
        try {
            database.createArgumentTopic(
                    "Test Topic",
                    "Test Content",
                    "testUser"
            );
            return;
        } catch (InvalidUserException e) {
            fail("Should throw Exceptions.InvalidUserException for non-existent user");
        }

        // Test creation with non-existent user
        try {
            database.createArgumentTopic(
                    "Test Topic",
                    "Test Content",
                    "nonExistentUser"
            );
            fail("Should throw Exceptions.InvalidUserException for non-existent user");
        } catch (InvalidUserException e) {
            assertEquals("User nonExistentUser not found", e.getMessage());
        }
    }

    @Test
    public void testAddComment() throws InvalidCredentialsException, DoesNotExistException, IOException {
        // Setup
        database.createUser("testUser", "Test@123");
        database.createArgumentTopic("Test Topic", "Test Content", "testUser");
        User user = database.getUser("testUser");
        ArgumentTopic topic = database.getArgumentTopics().get(0);

        // Test valid comment addition
        Comment comment = new Comment(true, "Test comment", user.getUsername());
        boolean result = database.addComment(topic, comment);
        assertTrue("Valid comment addition should return true", result);
        assertEquals("Topic should have 1 comment", 1, topic.getComments().size());

        // Test null comment
        result = database.addComment(topic, null);
        assertFalse("Adding null comment should return false", result);
    }

    @Test
    public void testFriendManagement() throws InvalidCredentialsException, InvalidUserException {
        // Setup users
        database.createUser("user1", "Test@123");
        database.createUser("user2", "Test@123");

        // Test adding friend
        boolean result = database.addFriend("user1", "user2");
        assertTrue("Adding valid friend should return true", result);
        assertTrue("Friend list should contain added user",
                database.getUser("user1").getFriendlist().contains("user2"));

        // Test removing friend
        result = database.removeFriend("user1", "user2");
        assertTrue("Removing existing friend should return true", result);
        assertFalse("Friend list should not contain removed user",
                database.getUser("user1").getFriendlist().contains("user2"));
    }

    @Test
    public void testBlockUser() throws InvalidCredentialsException, InvalidUserException {
        // Setup users
        database.createUser("user1", "Test@123");
        database.createUser("user2", "Test@123");

        // Test blocking user
        boolean result = database.blockUser("user1", "user2");
        assertTrue("Blocking valid user should return true", result);
        assertTrue("Blocked users list should contain blocked user",
                database.getUser("user1").getBlockedUsers().contains("user2"));

        // Test unblocking user
        result = database.unBlockUser("user1", "user2");
        assertTrue("Unblocking existing blocked user should return true", result);
        assertFalse("Blocked users list should not contain unblocked user",
                database.getUser("user1").getBlockedUsers().contains("user2"));
    }
//    public void testDeleteUser() throws InvalidCredentialsException, CouldNotDeleteException, IOException, InvalidUserException {
//        // Setup
//        database.createUser("deleteTest", "Test@123");
//        database.saveData(true);
//        boolean result = database.deleteUser("deleteTest", "Test@123");
//        assertTrue("Valid user deletion should return true", result);
//
//        // Verify user file is deleted
//        assertFalse("User file should be deleted",
//                new File("data/Users/deleteTest.txt").exists());
//
//        // Test deleting non-existent user
//        try {
//            database.deleteUser("nonExistent", "Test@123");
//            fail("Should throw Exceptions.CouldNotDeleteException for non-existent user");
//        } catch (CouldNotDeleteException e) {
//            assertEquals("Could not delete Profile.", e.getMessage());
//        }
//    }

    @Test
    public void testDeleteComment() throws InvalidCredentialsException,
            InvalidPostException, CouldNotDeleteException, IOException, DoesNotExistException {
        database.createUser("testUser","Test@123");
        database.createArgumentTopic("Test Topic", "Test Content", "testUser");
        ArgumentTopic topic = database.getArgumentTopics().get(0);
        Comment comment = new Comment(true, "Test database.Comment", "testUser");
        database.addComment(topic, comment);
        boolean result = database.deleteComment(topic,comment);
        assertTrue("Valid comment deletion should return true", result);
        assertFalse("Comment should be removed from database",
                database.getArgumentTopic(topic.getFileCode()).getComments().contains(comment));
        try {
            database.deleteComment(topic, comment);
            fail("Should throw Exceptions.CouldNotDeleteException for non-existent comment");
        } catch (CouldNotDeleteException e) {
            assertEquals("Comment not found", e.getMessage());
        }
    }

    @Test
    public void testDeleteArgumentTopic() throws InvalidCredentialsException,
            InvalidPostException, CouldNotDeleteException, IOException, InvalidUserException {
        // Setup
        database.createUser("testUser", "Test@123");
        database.createArgumentTopic("Test Topic", "Test Content", "testUser");
        ArgumentTopic topic = database.getArgumentTopics().get(0);

        // Test valid topic deletion
        try {
            boolean result = database.deleteArgumentTopic(topic);
            assertTrue("Valid topic deletion should return true", result);
            assertFalse("Topic should be removed from database",
                    database.getArgumentTopics().contains(topic));
        } catch (CouldNotDeleteException e) {
            fail("Delete argument topic should not throw error");
        }

        // Test deleting non-existent topic
        try {
            database.deleteArgumentTopic(topic);
            fail("Should throw Exceptions.CouldNotDeleteException for non-existent topic");
        } catch (CouldNotDeleteException e) {
            assertEquals("Topic doesn't exist", e.getMessage());
        }
    }

    @Test
    public void testSaveDatabase() throws InvalidCredentialsException, InvalidUserException, IOException {
        database.createUser("User1_Username", "User1_Password@");
        database.createArgumentTopic("ArgumentTopic1", "Content1", "User1_Username");
        database.createArgumentTopic("ArgumentTopic2", "Content2", "User1_Username");

        database.createUser("User2_Username", "User2_Password@");
        database.createArgumentTopic("ArgumentTopic3", "Content3", "User2_Username");
        database.createArgumentTopic("ArgumentTopic4", "Content4", "User2_Username");

        database.createUser("User3_Username", "User3_Password@");
        database.createArgumentTopic("ArgumentTopic5", "Content5", "User3_Username");
        database.createArgumentTopic("ArgumentTopic6", "Content6", "User3_Username");

        database.addFriend("User1_Username", "User2_Username");
        database.addFriend("User2_Username", "User3_Username");

        database.blockUser("User3_Username", "User1_Username");

        database.saveData(true);
    }

    @Test
    public void testLoadDatabase() throws InvalidCredentialsException, IOException, InvalidUserException {
        testSaveDatabase();

        database2.loadDatabase(userPaths, new boolean[userPaths.size()], true);

        boolean result = database.getUsers().equals(database2.getUsers()) &&
                database.getArgumentTopics().equals(database2.getArgumentTopics());

        assertTrue(Arrays.toString(database.getUsers().toArray()) + " equals " +
                Arrays.toString(database2.getUsers().toArray()), result);
    }

    @Test
    public void testLoadFeed() throws InvalidUserException, InvalidCredentialsException, IOException {
        // Setup
        database.createUser("user1", "Test@123");
        database.createUser("user2", "Test@123");
        database.createUser("user3", "Test@123");

        // Create friends
        database.addFriend("user1", "user2");
        database.addFriend("user1", "user3");

        // Create argument topics for friends
        database.createArgumentTopic("Topic1", "Content1", "user2");
        database.createArgumentTopic("Topic2", "Content2", "user3");

        ArgumentTopic topic1 = database.getArgumentTopics().get(0);
        ArgumentTopic topic2 = database.getArgumentTopics().get(1);

        // Set engagement levels for topics
        topic1.setEngagement(5);
        topic2.setEngagement(10);

        // Load feed for user1
        User user1 = database.getUser("user1");
        ArrayList<ArgumentTopic> feed = database.loadFeed(user1);

        // Validate results
        assertEquals("Feed should contain 2 topics", 2, feed.size());
        assertEquals("First topic in feed should be the one with highest engagement", topic2, feed.get(0));
        assertEquals("Second topic in feed should be the one with lower engagement", topic1, feed.get(1));
    }

}

