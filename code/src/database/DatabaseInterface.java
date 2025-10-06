package database;/* Team Project Phase 1 -- database.DatabaseInterface
 *
 * This Interface outlines the general functionality of the database.Database class.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */

import exceptions.*;

import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public interface DatabaseInterface {
    void loadDatabase(ArrayList<String> userIn, boolean[] locks, boolean test) throws IOException;

    ArgumentTopic processArgumentTopicFile(String filename, User user) throws IOException;

    boolean saveData(boolean test);

    boolean deleteComment(ArgumentTopic topic, Comment comment) throws CouldNotDeleteException;

    boolean createUser(String username, String pw) throws InvalidCredentialsException;

    boolean addComment(ArgumentTopic topic, Comment comment) throws DoesNotExistException;

    User getUser(String username) throws InvalidUserException;

    boolean validatePassword(String password) throws InvalidCredentialsException;

    void writeToFile(String data, String directory, String fileName, boolean append);

    boolean deleteUser(String username, String pw) throws CouldNotDeleteException, IOException;

    void createArgumentTopic(String title, String content, String authorUsername) throws InvalidUserException, IOException;

    void createArgumentTopic(String title, String content, String authorUsername, ImageIcon image)
            throws InvalidUserException, IOException;
    void createArgumentTopic(ArgumentTopic Topic) throws InvalidUserException, IOException;

    ArgumentTopic getArgumentTopic(String uuid) throws InvalidPostException;

    boolean deleteArgumentTopic(ArgumentTopic topic) throws CouldNotDeleteException;

    boolean addFriend(String addingUsername, String addedUserName) throws InvalidUserException;

    boolean removeFriend(String removingUser, String removedUser) throws InvalidUserException;

    boolean unBlockUser(String blockerUsername, String blockedUsername) throws InvalidUserException;

    void removeStringFromFile(String filePath, String target);

    ArrayList<ArgumentTopic> loadFeed(User user) throws InvalidUserException;
}
