package database;

import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Team Project Phase 1 -- database.Database
 * This class represents the database for the platform.
 * The database.Database class is responsible for managing user data, argument topics, and associated images.
 * Provides methods for loading data, creating and deleting users, managing argument topics, and performing friend and
 * block operations on users.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 3, 2024
 *
 */

public class Database implements Serializable {
    // Fields
    private static final double SIMILARITY_THRESHOLD = 0.5;
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
    private static final String SEPARATOR = "$%^";   // Separator used to organize data in files
    private static final String DELIMITER = "@@,@@";
    private static final String DIRECTORY = "data/posts/images";
    private static final Object o = new Object();   // Object used for synchronizing access
    private static boolean[] locks;      // Array to manage file access locks

    private ArrayList<User> users;  // List of all users in the database
    private int rank;
    private ArrayList<ArgumentTopic> argumentTopics;  // List of all argument topics
    private String userOut; // Output destination for user data
    private String argumentTopicIn;    // Input source for argument topics
    private ArrayList<Image> Images;    // List of images associated with arguments

    // Constructor
    public Database(ArrayList<String> userIn) {
        locks = new boolean[userIn.size()];
        users = new ArrayList<User>();
        argumentTopics = new ArrayList<ArgumentTopic>();

        try {
            loadDatabase(userIn, locks, false);
        } catch (IOException e) {
            System.out.println("Database loading failed, File IO error encountered.");
        }
    }

    public Database() {
        users = new ArrayList<User>();
        argumentTopics = new ArrayList<ArgumentTopic>();
        rank = 0;
    }

    /*
     * This method loads user data from files and creates users.
     * It synchronizes access to files using locks and handles exceptions.
     * @param userIn ArrayList of user file paths
     */
    public void loadDatabase(ArrayList<String> userIn, boolean[] locks, boolean test) throws IOException {
        String userPath = "code/data/users/";
        String postPath = "code/data/posts/";
        if (test) {
            userPath = "data/testusers/";
            postPath = "data/testposts/";
        }

        for (int i = 0; i < userIn.size(); i++) {
            synchronized (o) {
                if (locks[i]) {
                    continue;
                } else {
                    locks[i] = true;
                }
            }

            File userFile = new File(userPath + userIn.get(i));

            if (!userFile.exists()) {
                throw new IOException(userFile + " does not exist.");
            }

            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                ArgumentTopic at;

                String line = br.readLine();
                User user;
                try {
                    user = new User(line.split(DELIMITER)[0], line.split(DELIMITER)[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }

                line = br.readLine();

                int section = 0;
                while (line != null) {
                    if (line.equals("$%^")) {
                        section++;
                        line = br.readLine();
                        continue;
                    }
                    switch (section) {
                        case 0: // Argument topics
                            try {
                                at = processArgumentTopicFile(postPath + line, user);
                                if (at == null) {
                                    System.out.println("Incorrectly formatted file: " + postPath + line);
                                } else {
                                    argumentTopics.add(at);
                                    user.addToArgumentTopicHistory(at);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("IO Exception");
                            }
                            break;
                        case 1: // Friends List
                            user.getFriendlist().add(line);
                            break;
                        case 2: // Blocked users list
                            user.getBlockedUsers().add(line);
                            break;
                    }
                    line = br.readLine();
                }
                users.add(user);
                if (user.getUsername().length() > rank) {
                    rank = user.getUsername().length();
                }
            } catch (IOException e) {
                throw new IOException("File read error: " + userFile);
            }
        }
    }

    public Comment getComment(ArgumentTopic argumentTopic, String commentUUID) throws InvalidPostException {
        for (Comment comment : argumentTopic.getComments()) {
            synchronized (o) {
                if (comment.getCommentCode().equals(commentUUID)) {
                    return comment;
                }
            }
        }
        throw new InvalidPostException("Comment not found");

    }

    public boolean deleteComment(ArgumentTopic topic, Comment comment) throws CouldNotDeleteException {
        for (Comment comment1 : topic.getComments()) {
            synchronized (o) {
                if (comment1.equals(comment)) {
                    topic.getComments().remove(comment1);
                    removeStringFromFile("data/posts/" + topic.getFileCode(), comment1.toString());
                    return true;
                }
            }
        }
        throw new CouldNotDeleteException("Comment not found");
    }

    /*
     * This method adds a comment to an argument topic and returns true if the comment was successfully added.
     * It also updates the corresponding file.
     * @param topic The argument topic to add the comment to.
     * @param comment The comment to add.
     */
    public ArgumentTopic processArgumentTopicFile(String filename, User user) throws IOException {
        ArgumentTopic argumentTopic = null;
        File argumentTopicInFile = new File(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(argumentTopicInFile))) {
            String line = br.readLine();
            boolean firstLine = true;
            while (line != null) {
                String[] columns = line.split(DELIMITER);
                if (firstLine) {
                    try {
                        if (columns[3].equals("null")) {
                            argumentTopic = new ArgumentTopic(filename, columns[0], columns[1], user);
                        } else {
                            ImageIcon img = new ImageIcon(columns[3]);
                            argumentTopic = new ArgumentTopic(filename, columns[0], columns[1], user, img);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                    firstLine = false;
                } else {
                    try {
                        argumentTopic.appendComment(new Comment(Boolean.parseBoolean(columns[0]),
                                columns[1],
                                columns[2],
                                columns[3],
                                Integer.parseInt(columns[4]),
                                Integer.parseInt(columns[5])));
                    } catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                }
                line = br.readLine();
            }
            return argumentTopic;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("File read error: " + filename);
        }
    }

    public void upvoteComment (Comment comment) {
        comment.incrementUpvotes();
    }

    public void downvoteComment(Comment comment) {
        comment.incrementDownvotes();
    }

    /*
     * This method saves user data to files and returns true if the data was successfully saved.
     * It also updates the corresponding file.
     * @return true if the data was successfully saved
     * @throws IOException
     */
    public synchronized boolean saveData(boolean test) {
        String userPath = "code/data/users/";
        String postPath = "code/data/posts/";
        if (test) {
            userPath = "data/testusers/";
            postPath = "data/testposts/";
        }

        File folder = new File(postPath);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }

        // create individual user files
        for (User user : users) {
            String filePath = userPath + user.getUsername() + ".txt";
            File userFile = new File(filePath);
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(userFile))) {
                // print username and password for user constructor
                pw.println(String.format("%s%s%s", user.getUsername(), DELIMITER, user.getPassword()));

                // print file paths for each database.ArgumentTopic
                for (ArgumentTopic argumentTopic : user.getArgumentTopicHistory()) {

                    pw.println(argumentTopic.getFileCode());
                    File argumentTopicFile = new File(postPath + argumentTopic.getFileCode());
                    try (PrintWriter pw2 = new PrintWriter(argumentTopicFile)) {
                        // print data to input into database.ArgumentTopic's constructor
                        pw2.println(String.format("%s%s%s%s%s%s%s", argumentTopic.getTitle(), DELIMITER,
                                argumentTopic.getContent(), DELIMITER, argumentTopic.getAuthor().getUsername(),
                                DELIMITER, argumentTopic.getImagepath()));

                        // print comment data for each comment
                        for (Comment comment : argumentTopic.getComments()) {
                            pw2.println(String.format("%b%s%s%s%s%s%s%s%d%s%d", comment.getSide(), DELIMITER,
                                    comment.getContent(), DELIMITER, comment.getAuthor(), DELIMITER,
                                    comment.getCommentCode(), DELIMITER, comment.getUpvotes(), DELIMITER,
                                    comment.getDownvotes()));
                        }
                    } catch (IOException e) {
                        return false;
                    }
                }
                pw.println(SEPARATOR);

                // print username of each friended user
                for (String friend : user.getFriendlist()) {
                    pw.println(friend);
                }
                pw.println(SEPARATOR);

                // print username of each blocked user
                for (String blockedUser : user.getBlockedUsers()) {
                    pw.println(blockedUser);
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Creates a new user in the system with a specified username and password.
     * Checks if the username is unique and if the password meets complexity requirements.
     * @param username the username for the new user
     * @param pw the password for the new user
     * @return true if user creation is successful, false otherwise
     * @throws InvalidCredentialsException if username is taken or password is invalid
     */

    public boolean createUser(String username, String pw) throws InvalidCredentialsException, InvalidUserException {
        String allUserData = "";
        String userSpecificData = "";
        String outputFileName = "";
        String userFileName = "";

        // Check for null values in username or password
        if (username == null || pw == null) {
            throw new InvalidCredentialsException("Please enter a username and password");
        }

        if (username.contains(",")) {
            throw new InvalidCredentialsException("Your username can't contain a comma");
        }
        // Check for duplicate username
        for (User user1 : users) {
            synchronized (o) {
                if (user1.getUsername().equals(username)) {
                    throw new InvalidCredentialsException("This username is already taken");
                }
            }
        }

        // Check for valid password and create user if valid
        if (!validatePassword(pw)) {
            throw new InvalidCredentialsException("Password must be at least 8 characters long, " +
                    "and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character.");
        } else {
            User user = new User(username, pw);
            synchronized (o) {
                users.add(user);
//                if (user.getUsername().length() > rank) {
//                    rank = user.getUsername().length();
//                }

            }

            outputFileName = "allUsers.txt";
            userFileName = username + ".txt";
            allUserData = userFileName + "," + user.getUsername() + "," + user.getPassword();
            writeToFile(allUserData,"data",outputFileName, true);
        }
        return true;
    }
    /*
     * Adds a comment to an argument topic.
     * @throws DoesNotExistException if the argument topic does not exist
     * @throws DoesNotExistException if the comment is null or invalid
     * @param topic the argument topic to add the comment to
     * @param comment the comment to add
     * @return true if the comment was added successfully, false otherwise
     */
    public boolean addComment(ArgumentTopic topic, Comment comment) throws DoesNotExistException {
        try {
            synchronized (o) {
                if (!argumentTopics.contains(topic)) {
                    throw new DoesNotExistException("This post doesn't exist");
                }
            }

            if (comment == null) {
                return false;
            } else {
                topic.getComments().add(comment);
                return true;
            }
        } catch (DoesNotExistException e) {
            return false;
        }
    }


    /**
     * Retrieves a user from the database if they exist.
     * @param username the user to search for in the database
     * @return the database.User object if found
     * @throws InvalidUserException if the user is not found
     */
    public User getUser(String username) throws InvalidUserException {
        for (User user1 : users) {
            synchronized (o) {
                if (user1.getUsername().equals(username)) {
                    return user1;
                }
            }
        }

        throw new InvalidUserException("User " + username + " not found");

    }

    /**
     * Validates a password against predefined complexity requirements.
     * @param password the password to validate
     * @return true if the password meets the requirements, false otherwise
     * @throws InvalidCredentialsException if the password is invalid
     */
    public boolean validatePassword(String password) throws InvalidCredentialsException {
        try {
            synchronized (o) {
                if (password.matches(PASSWORD_PATTERN)) {
                    return true;
                } else {
                    throw new InvalidCredentialsException("Password doesn't meet requirements");
                }
            }
        }
        catch (InvalidCredentialsException e) {
            return false;
        }
    }

    /**
     * Writes a string of data to a specified file.
     * @param data the data to write
     * @param fileName the file to write to
     */
    public synchronized void writeToFile(String data, String directory, String fileName, boolean append) {
        // Create the full path by combining directory and file name
        File dir = new File(directory);
        File file = new File(dir, fileName);

        try {
            // Ensure the directory exists
            if (!dir.exists()) {
                Files.createDirectories(Paths.get(directory));
            }

            // Write data to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {
                bw.write(data);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Invalid file or directory");
        }
    }

    /**
     * Deletes a user from the database if the username and password match.
     * Removes user data from the database and deletes the user's file.
     * @param username the username of the user to delete
     * @param pw the password of the user to delete
     * @return true if the user was deleted successfully, false otherwise
     * @throws CouldNotDeleteException if the user cannot be deleted
     */
    public boolean deleteUser(String username, String pw) throws CouldNotDeleteException, IOException {
        for (User user1 : users) {
            synchronized (o) {
                if (username.equals(user1.getUsername())) {
                    if (user1.getPassword().equals(pw)) {
                        // Delete user file
                        String path = String.format("code/data/users/%s.txt", user1.getUsername());
                        File file = new File(path);
                        if (file.exists()) {
                            if (file.delete()) {
                                System.out.println("User File deleted successfully");
                            } else {
                                throw new CouldNotDeleteException("Could not delete Profile.");
                            }
                        }
                        users.remove(user1);

                        // Remove user data from 'AllUsers.txt' file
                        String filePath = "code/data/allUsers.txt";
                        String fileContent = "";

                        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                fileContent += line + "\n";
                            }

                            System.out.println("File content:\n" + fileContent);
                        }

                        String textToRemove = user1.getUsername() + ".txt";
                        String modifiedContent = fileContent.replace(textToRemove, "");
                        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                            writer.print(modifiedContent);
                            System.out.println("File overwritten successfully.");
                            return true;
                        }
                    }
                }
            }
        }
        throw new CouldNotDeleteException("Could not delete Profile.");
    }
    /**
     * Creates a new argument topic in the database if the author is a valid user.
     * @param title the title of the argument topic
     * @param content the content of the argument topic
     * @param authorUsername the user who created the argument topic
     * @return true if the argument topic was created successfully, false otherwise
     */
    public void createArgumentTopic(String title, String content, String authorUsername)
            throws InvalidUserException, IOException {
        ArgumentTopic newTopic = new ArgumentTopic(title, content, getUser(authorUsername));
        synchronized (o) {
            argumentTopics.add(newTopic);
            writeToFile(newTopic.getFileCode(), "code/data", "allPosts.txt", true);
            getUser(authorUsername).addToArgumentTopicHistory(newTopic);
        }
    }

    public void createArgumentTopic(ArgumentTopic topic) throws InvalidUserException, IOException {
        synchronized (o) {
            argumentTopics.add(topic);
            writeToFile(topic.getFileCode(), "code/data", "allPosts.txt", true);
            topic.getAuthor().addToArgumentTopicHistory(topic);
        }
    }

    public void createArgumentTopic(String title, String content, String authorUsername, ImageIcon image)
            throws InvalidUserException, IOException {
            ArgumentTopic newTopic = new ArgumentTopic(title, content, getUser(authorUsername), image);
            synchronized (o) {
                argumentTopics.add(newTopic);
                writeToFile(newTopic.getFileCode(), "code/data", "allPosts.txt", true);
                getUser(authorUsername).addToArgumentTopicHistory(newTopic);
            }
    }

    /*
     * Retrieves an argument topic from the database if it exists.
     * @param uuid the unique identifier of the argument topic
     * @return the argument topic if found, null otherwise
     * @throws InvalidPostException if the argument topic does not exist
     */
    public ArgumentTopic getArgumentTopic(String uuid) throws InvalidPostException {
        for (ArgumentTopic argumentTopic1 : argumentTopics) {
            synchronized (o) {
                if (argumentTopic1.getFileCode().equals(uuid)) {
                    return argumentTopic1;
                }
            }
        }
        throw new InvalidPostException("ArgumentTopic not found");
    }
    /*
     * Deletes an argument topic from the database if it exists.
     * @param topic the argument topic to delete
     * @return true if the argument topic was deleted successfully, false otherwise
     * @throws CouldNotDeleteException if the argument topic cannot be deleted
     */
    public boolean deleteArgumentTopic(ArgumentTopic topic) throws CouldNotDeleteException {
        String filePath = topic.getFileCode(); //topic file name
        File file = new File(filePath);

        synchronized (o) {
            if (!argumentTopics.contains(topic)) { //throws exception if
                throw new CouldNotDeleteException("Topic doesn't exist");
            }
        }

        synchronized (o) {
            argumentTopics.remove(topic);
        }

        file.delete(); //deletes specific file for that database.ArgumentTopic
        for (User user: users) {
            synchronized (o) {
                String userFileName = user.getUsername() + ".txt";
                removeStringFromFile(userFileName, topic.getFileCode());
            }
        }

        removeStringFromFile("AllPosts.txt", topic.getFileCode());
        return true;

    }

    //verifying login
    public boolean verifyLogin(String userName, String pw) throws InvalidUserException, InvalidCredentialsException {
        return getUser(userName).getPassword().equals(pw);
    }

    /**
     * Adds a user to another user's friend list if both users are not blocked.
     * @param addingUserName the user adding a friend
     * @param addedUserName the user being added as a friend
     * @return true if the friend was added successfully, false otherwise
     * @throws InvalidUserException if either user does not exist
     */
    public boolean addFriend(String addingUserName, String addedUserName) throws InvalidUserException {
        try {
            if (!getUser(addingUserName).getBlockedUsers().contains(addedUserName) ||
                    !getUser(addedUserName).getBlockedUsers().contains(addingUserName) ) {
                if (getUser(addingUserName).getFriendlist().contains(addedUserName)) {
                    return false;
                }
                getUser(addingUserName).getFriendlist().add((addedUserName));
                getUser(addedUserName).getFriendlist().add((addingUserName));
                return true;
            } else {
                return false;
            }
        } catch (InvalidUserException e) {
            return false;
        }
    }

    /**
     * Removes a user from another user's friend list.
     * @param removingUser the user removing a friend
     * @param removedUser the user being removed as a friend
     * @return true if the friend was removed successfully, false otherwise
     * @throws InvalidUserException if either user does not exist
     */
    public boolean removeFriend(String removingUser, String removedUser) throws InvalidUserException {
        try {
            User removing = getUser(removingUser);
            if (removing.getFriendlist().contains(removedUser)) {
                removing.getFriendlist().remove(getUser(removedUser).getUsername());
                return true;
            }
            return false;
        } catch (InvalidUserException e) {
            return false;
        }
    }

    /**
     * Adds a user to another user's block list.
     * @param blockerName the user blocking another user
     * @param blockedName the user being blocked
     * @return true if the block was successful, false otherwise
     * @throws InvalidUserException if either user does not exist
     */
    public boolean blockUser(String blockerName, String blockedName) throws InvalidUserException {
        try {
            getUser(blockerName).getBlockedUsers().add(blockedName);
            return true;
        } catch (InvalidUserException e){
            return false;
        }
    }

    /**
     * Removes a user from another user's block list.
     * @param blockerName the user who is unblocking another user
     * @param blockedName the user being unblocked
     * @return true if the unblock was successful, false otherwise
     * @throws InvalidUserException if either user does not exist
     */
    public boolean unBlockUser(String blockerName, String blockedName) throws InvalidUserException {
        try {
            User blocker = getUser(blockerName);
            User blocked = getUser(blockedName);
            if (blocker.getBlockedUsers().contains(blocked.getUsername())) {
                blocker.getBlockedUsers().remove(blocked.getUsername());
                return true;
            }
            return false;
        }
        catch (InvalidUserException e){
            return false;
        }
    }

    /**
     * Removes a string from a given file.
     * @param filePath the path of the file from which to remove the string
     * @param target the string to remove
     */
    public void removeStringFromFile(String filePath, String target) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            content = content.replace(target, "");
            Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed to remove string due to IO issues");
        }
    }

    public ArrayList<ArgumentTopic> getArgumentTopics() {
        return argumentTopics;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<String> searchUsers(String query) {
        System.out.println("\n------------\n");
        ArrayList<String> output = new ArrayList<>();
        for (User user : users) {
            System.out.printf("Query: %s\nUser: %s\n", query, user.getUsername());
            if (getSimilarity(wordToVec(query), wordToVec(user.getUsername())) > 0.4) {
                output.add(user.getUsername());
            }
        }
        System.out.println("\n------------\n");
        output.sort((a, b) -> Double.compare(getSimilarity(wordToVec(query), wordToVec(b)),
                getSimilarity(wordToVec(query), wordToVec(a))));
        return output;
    }

    public int[] wordToVec(String str) {
        int[] v = new int[10];
        for (int i = 0; i < 10; i++) {
            if (i < str.length()) {
                if (i == 0) {
                    v[i] = (int) (str.charAt(i) + str.charAt(i + 1)) / 2;
                } else if (i == str.length() - 1) {
                    v[i] = (int) (str.charAt(i) + str.charAt(i - 1)) / 2;
                } else {
                    v[i] = (int) (str.charAt(i) + str.charAt(i + 1) + str.charAt(i - 1)) / 3;
                }
            } else {
                v[i] = 0; // Padding for vectors shorter than rank
            }
        }
        return v;
    }

    public double getSimilarity(int[] vA, int[] vB) {
        double magA = 0;
        double magB = 0;
        double dp = 0;
        if (vA.length != vB.length) {
            return -1; // Error case
        }

        for (int i = 0; i < vA.length; i++) {
            magA += Math.pow(vA[i], 2);
            magB += Math.pow(vB[i], 2);
            dp += vA[i] * vB[i];
        }

        magA = Math.sqrt(magA);
        magB = Math.sqrt(magB);

        if (magA == 0 || magB == 0) {
            return 0; // Avoid division by zero
        }

        double cosine = dp / (magA * magB);
        cosine = Math.max(-1.0, Math.min(1.0, cosine)); // Clamp to avoid NaN

        System.out.printf("vA: %s\nvB: %s\ndot product: %f\n|vA|: %f\n|vB|: %f\ntheta: %f\n\n",
                Arrays.toString(vA), Arrays.toString(vB), dp, magA, magB, Math.toDegrees(Math.acos(cosine)));

        return Math.toDegrees(Math.acos(cosine));
    }

    /**
     * Loads News Feed
     * @param user is the user the feeds needs to load for.
     * @throws InvalidUserException if the user does not exist.
     */
    public ArrayList<ArgumentTopic> loadFeed(User user) throws InvalidUserException {
        ArrayList<ArgumentTopic> topics = new ArrayList<ArgumentTopic>();
        if (user.getFriendlist().isEmpty()) {
            throw new RuntimeException("friend list empty");
        }
        for (String friend : user.getFriendlist()) {
            for (ArgumentTopic at : getUser(friend).getArgumentTopicHistory()) {
                topics.add(at);
            }
        for (ArgumentTopic at : user.getArgumentTopicHistory()) {
            topics.add(at);
        }

        }
        topics.sort((a, b) -> Integer.compare(b.getEngagement(), a.getEngagement()));
        return topics;

    }

    public ArrayList<String> getRandomUsers(String userName) throws InvalidUserException {
        ArrayList<String> randomUserNames = new ArrayList<>();
        Random random = new Random();
        for (User user : users){
            if (random.nextInt(10) > 5 && !getUser(userName).getFriendlist().contains(user.getUsername()) ){
                randomUserNames.add(user.getUsername());
            }
        }

        return randomUserNames;
    }

}
