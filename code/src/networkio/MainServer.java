package networkio;

import database.ArgumentTopic;
import database.Comment;
import database.Database;
import database.User;
import exceptions.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Team Project Phase 2 -- networkio.MainServer
 *
 * This class contains the server that recieves database requests from clients and returns messages
 * after calling methods from the database.Database class
 *
 * @author Raghav Gangatirkar, Atman Singh,
 * Aditya Pachpande, CS 180
 *
 * @version November 10, 2024
 *
 */

public class MainServer implements ServerInterface, Runnable {
    static Socket socket;
    static ServerSocket serverSocket;
    static Database db;
    public MainServer() {
        db = new Database();
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Message msg = (Message) in.readObject();

                switch (msg.getMessageType()) {
                    case DELETE_COMMENT:
                        try {
                            ArgumentTopic argumentTopic =
                                    (ArgumentTopic) db.getArgumentTopic(msg.getData().get(0) + "");
                            Comment comment = (Comment) db.getComment(argumentTopic,
                                    msg.getData().get(1) + "");
                            db.deleteComment(argumentTopic, comment);
                            db.saveData(false);
                            ArrayList data = new ArrayList();
                            data.add(argumentTopic);
                            out.writeObject(new Message(Message.MessageType.SUCCESS, data));
                            out.flush();
                        } catch (CouldNotDeleteException | IndexOutOfBoundsException | InvalidPostException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case CREATE_USER:
                            try {
                                String username = (String) msg.getData().get(0);
                                String pw = (String) msg.getData().get(1);
                                db.createUser(username, pw);
                               db.addFriend(username,"duelogue");
                                out.writeObject(new Message(Message.MessageType.SUCCESS,
                                        "User created succesfully"));
                                out.flush();
                            } catch (Exception e) {
                                out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                                out.flush();
                            }
                        break;
                    case LOGIN:
                        try {
                            String username = (String) msg.getData().get(0);
                            String pw = (String) msg.getData().get(1);
                            if (db.verifyLogin(username,pw)) {
                                out.writeObject(new Message(Message.MessageType.SUCCESS,
                                        "Logged in succesfully"));
                                out.flush();
                            } else {
                                out.writeObject(new Message(Message.MessageType.ERROR,
                                        "Incorrect username/password"));
                                out.flush();
                            }
                        } catch (InvalidUserException e) {
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        } catch (InvalidCredentialsException e){
                            out.writeObject(new Message(Message.MessageType.ERROR, "Incorrect password"));
                            out.flush();
                        }
                        break;
                    case GET_USER:
                        try {
                            String username = (String) msg.getData().get(0);
                            User user = db.getUser(username);
                            ArrayList data = new ArrayList();
                            data.add(user);
                            out.writeObject(new Message(Message.MessageType.USER, data));
                            out.flush();
                        } catch (InvalidUserException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case ADD_COMMENT:
                        try {
                            String argumentTopicID = (String) msg.getData().get(0);
                            ArgumentTopic argumentTopic = db.getArgumentTopic(argumentTopicID);
                            String commentContent = (String) msg.getData().get(1);
                            String username = (String) msg.getData().get(2);
                            Boolean side = (Boolean) msg.getData().get(3);
                            Comment comment = new Comment(side, commentContent, username);
                            db.addComment(argumentTopic, comment);
                            db.saveData(false);
                            ArrayList data = new ArrayList();
                            data.add(argumentTopic);
                            ArrayList<Comment> commentList = argumentTopic.getComments();
                            data.add(commentList.get(commentList.size() - 1));
                            out.writeObject(new Message(Message.MessageType.SUCCESS, data));
                            out.flush();
                        } catch (DoesNotExistException | IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        } catch (InvalidPostException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case DELETE_USER:
                        try {
                            String username = (String) msg.getData().get(0);
                            String pw = (String) msg.getData().get(1);
                            db.deleteUser(username, pw);
                            out.writeObject(new Message(Message.MessageType.SUCCESS,
                                    "User deleted successfully"));
                            out.flush();
                        } catch (CouldNotDeleteException | IndexOutOfBoundsException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case UPVOTE:
                        try {
                            ArgumentTopic topic = db.getArgumentTopic((String) msg.getData().get(0));
                            Comment comment = db.getComment(topic, (String) msg.getData().get(1));
                            String user = (String) msg.getData().get(2);
                            comment.incrementUpvotes();
                            ArrayList data = new ArrayList<>();
                            data.add(comment.getUpvotes());
                            out.writeObject(new Message(Message.MessageType.SUCCESS, data));
                            out.flush();
                            }

                        catch (InvalidPostException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case DOWNVOTE:
                        try {
                            ArgumentTopic topic = db.getArgumentTopic((String) msg.getData().get(0));
                            Comment comment = db.getComment(topic, (String) msg.getData().get(1));
                            String user = (String) msg.getData().get(2);
                            comment.incrementDownvotes();
                            ArrayList data = new ArrayList<>();
                            data.add(comment.getDownvotes());
                            out.writeObject(new Message(Message.MessageType.SUCCESS, data));
                            out.flush();
                        }

                        catch (InvalidPostException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case CREATE_ARGUMENT_TOPIC:
                        try {
                            String title = (String) msg.getData().get(0);
                            String content = (String) msg.getData().get(1);
                            String authorUsername = (String) msg.getData().get(2);
                            ArgumentTopic topic = new ArgumentTopic(title, content, db.getUser(authorUsername));
                            db.createArgumentTopic(topic);
                            out.writeObject(new Message(Message.MessageType.SUCCESS,
                                    "Argument topic created succesfully " + topic.getFileCode()));
                            System.out.println("Data: " + Arrays.toString(msg.getData().toArray()) + msg.getData().size());
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case CREATE_ARGUMENT_TOPIC_IMAGE:
                        try {
                            String title = (String) msg.getData().get(0);
                            String content = (String) msg.getData().get(1);
                            String authorUsername = (String) msg.getData().get(2);
                            ImageIcon image = (ImageIcon) msg.getData().get(3);
                            db.createArgumentTopic(title, content, authorUsername, image);
                            out.writeObject(new Message(Message.MessageType.SUCCESS,
                                    "Argument topic created succesfully"));
                            out.flush();
                        } catch (InvalidUserException | IOException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case GET_ARGUMENT_TOPIC:
                        try {
                            String uuid = (String) msg.getData().get(0);
                            ArgumentTopic argumentTopic = db.getArgumentTopic(uuid);
                            ArrayList data = new ArrayList();
                            data.add(argumentTopic);
                            out.writeObject(new Message(Message.MessageType.SUCCESS, data));
                            out.flush();
                        } catch (InvalidPostException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case DELETE_ARGUMENT_TOPIC:
                        try {
                            ArgumentTopic argumentTopic = db.getArgumentTopic((String)msg.getData().get(0));
                            if (db.deleteArgumentTopic(argumentTopic)) {
                                out.writeObject(new Message(Message.MessageType.SUCCESS, "Success!"));
                                out.flush();
                            } else {
                                out.writeObject(new Message(Message.MessageType.ERROR, "Could not be deleted"));
                                out.flush();
                            }
                        } catch (CouldNotDeleteException | IndexOutOfBoundsException  | InvalidPostException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case CHANGE_UPVOTE_STATUS:
                        break;
                    case ADD_FRIEND:
                        try {
                            String addingUsername = (String) msg.getData().get(0);
                            String addedUsername = (String) msg.getData().get(1);
                            if (db.addFriend(addingUsername, addedUsername)) {
                                out.writeObject(new Message(Message.MessageType.SUCCESS, "Success!"));
                                out.flush();
                            } else {
                                out.writeObject(new Message(Message.MessageType.ERROR,
                                        "Could not be Friended/You are already a Friend"));
                                out.flush();
                            }
                        } catch (InvalidUserException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case VIEW_NEWS_FEED:
                        try {
                            ArrayList data = new ArrayList();
                            String userNameToView = (String) msg.getData().get(0);
                            ArrayList<ArgumentTopic> newsFeed = db.loadFeed(db.getUser(userNameToView));
                            data.add(newsFeed);
                            out.writeObject(new Message(Message.MessageType.NEWS_FEED, data));
                            out.flush();
                        } catch (InvalidUserException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
//                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.writeObject(new Message(Message.MessageType.SUCCESS, e.getMessage()));
                            out.flush();
                            continue;
                        }
                        break;
                    case REMOVE_FRIEND:
                        try {
                            String removingUser = (String) msg.getData().get(0);
                            String removedUser = (String) msg.getData().get(1);
                            if (db.removeFriend(removingUser, removedUser)) {
                                out.writeObject(new Message(Message.MessageType.SUCCESS, "Success!"));
                                out.flush();
                            } else {
                                out.writeObject(new Message(Message.MessageType.ERROR, "Could not be removed"));
                                out.flush();
                            }
                        } catch (InvalidUserException e){
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case BLOCK_USER:
                        try {
                            String blockerName = (String) msg.getData().get(0);
                            String blockedName = (String) msg.getData().get(1);
                            db.blockUser(blockerName, blockedName);
                            out.writeObject(new Message(Message.MessageType.SUCCESS, "Success!"));
                            out.flush();
                        } catch (InvalidUserException e){
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case UNBLOCK_USER:
                        try {
                            String blockerName = (String) msg.getData().get(0);
                            String blockedName = (String) msg.getData().get(1);
                            if (db.unBlockUser(blockerName, blockedName)) {
                                out.writeObject(new Message(Message.MessageType.SUCCESS, "Success!"));
                                out.flush();
                            } else {
                                out.writeObject(new Message(Message.MessageType.ERROR, "Could not be removed"));
                                out.flush();
                            }
                        } catch (InvalidUserException e){
                            e.printStackTrace();
                            out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                            out.flush();
                        }
                        break;
                    case GET_RANDOM_USERS:
                        try {
                            String username = (String) msg.getData().get(0);
                            out.writeObject(new Message(Message.MessageType.USER_LIST, db.getRandomUsers(username)));
                            out.flush();
                        }
                        catch (InvalidUserException e){
                        e.printStackTrace();
                        out.writeObject(new Message(Message.MessageType.ERROR, e.getMessage()));
                        out.flush();
                    }
                        break;
                    case GET_SEARCH_RESULTS:
                        String query = (String) msg.getData().get(0);
                        ArrayList<String> results = db.searchUsers(query);
                        ArrayList data = new ArrayList();
                        data.add(results);
                        out.writeObject(new Message(Message.MessageType.USER_LIST, data));
                        out.flush();
                        break;
                    case SAVE_DATA:
                        db.saveData(false);
                        break;
                    default:
                        out.writeObject(new Message(Message.MessageType.ERROR,
                                "networkio.Message type not handled"));
                        out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        serverSocket = new ServerSocket(4201);
        MainServer mainServer = new MainServer();
        ArrayList<String> results = new ArrayList<String>();

        File folder = new File("code/data/Users/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

//        if (results != null) {
//            boolean[] locks = new boolean[results.size()];
//            for (int i = 0; i < 1; i ++) {
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            db.loadDatabase(results, locks, false);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            throw new RuntimeException(e);
//                        }
//                    }
//                });
//                t.start();
//            }
//        }
        boolean[] locks = new boolean[results.size()];
        db.loadDatabase(results, locks, false);
        System.out.println("Database: " + Arrays.toString(db.getArgumentTopics().toArray()));
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    System.out.println("Database: " + Arrays.toString(db.getArgumentTopics().toArray()));
//                }
//            }
//        });
//        t.start();

        for (int i = 0; i < 100000000; i++) {
            socket = serverSocket.accept();
            Thread thread = new Thread(mainServer);
            thread.start();
        }
    }



}
