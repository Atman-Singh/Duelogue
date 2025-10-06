package networkio;

import database.ArgumentTopic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class Client implements ClientInterface, Serializable {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 4201;


//READ JAVADOCS INSTRUCTIONS!!!

    public static void main(String[] args) throws IOException {
        String clientUsername = "";
        String clientPassword;
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

        String message = "";

        String options = "Options (input number):\n1. Delete database.Comment\n2.Get database.User\n" +
                "3. Add database.Comment\n4. Delete database.User\n5. Create Argument Topic\n6.Get Argument Topic\n" +
                "7. Delete Argument Topic\n8. Add Friend\n9. Remove Friend\n10. Block database.User\n11. Unblock database.User\n" +
                "12. View news feed\n13. Quit";

        try {
            boolean userValidated = false;
            boolean userCreated = false;

            ArrayList data;
            String argumentTopicUUID;
            String commentContent;
            String commentUUID;
            String username;
            String content;
            String title;
            String pw;
            int option = 0;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //TODO: CREATE USER
            boolean validOption = false;
            while (!validOption) {
                System.out.println("Select whether you want to login or create a new account");
                System.out.println("1. Create New account");
                System.out.println("2. Login to existing account");
                option = sc.nextInt();
                if (option == 1 || option == 2) {
                    validOption = true;
                }
                else {
                    System.out.println("Please select a valid option");
                }
                sc.nextLine();
            }

            if (option == 1) {
                while (!userCreated) {
                    data = new ArrayList();
                    System.out.println("[Create database.User]");
                    System.out.println("Input username:");
                    username = sc.nextLine();
                    System.out.println("Input password:");
                    pw = sc.nextLine();
                    data.add(username);
                    data.add(pw);
                    oos.writeObject(new Message(Message.MessageType.CREATE_USER, data));
                    oos.flush();
                    Message response = (Message) ois.readObject();
                    if (response.getMessageType().equals(Message.MessageType.SUCCESS)) {
                        System.out.println("success");
                        userCreated = true;

                    } else {
                        System.out.println(response.getMessage());
                    }
                }
            }

            while (!userValidated) {
                data = new ArrayList();
                System.out.println("[Login]");
                System.out.println("Input username:");
                username = sc.nextLine();
                System.out.println("Input password:");
                pw = sc.nextLine();
                data.add(username);
                data.add(pw);
                oos.writeObject(new Message(Message.MessageType.LOGIN, data));
                oos.flush();
                Message response = (Message) ois.readObject();
                if (response.getMessageType().equals(Message.MessageType.SUCCESS)) {
                    clientUsername = username;

                        userValidated = true;
                } else {
                    System.out.println(response.getMessage());
                }
            }

            while (!message.equals("13")) {

                System.out.print(options);
                System.out.println();
                message = sc.nextLine();

                data = new ArrayList();
                argumentTopicUUID = "";
                commentUUID = "";
                commentContent = "";
                username = "";
                content = "";
                title = "";
                pw = "";

                switch (message) {
                    case "1":
                        System.out.println("Input argument topic uuid:");
                        argumentTopicUUID = sc.nextLine() + ".txt";
                        System.out.println("Input comment uuid:");
                        commentUUID = sc.nextLine();
                        data = new ArrayList();
                        data.add(argumentTopicUUID);
                        data.add(commentUUID);
                        oos.writeObject(new Message(Message.MessageType.DELETE_COMMENT, data));
                        oos.flush();
                        Message response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "2":
                        System.out.println("Input username:");
                        username = sc.nextLine();
                        data = new ArrayList();
                        data.add(username);
                        oos.writeObject(new Message(Message.MessageType.GET_USER, data));
                        oos.flush();
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "3":
                        System.out.println("Input argument topic uuid:");
                        argumentTopicUUID = sc.nextLine();
                        System.out.println("Input comment content: ");
                        commentContent = sc.nextLine();
                        System.out.println("Enter your side: p for pro and a for against");
                        String side = sc.nextLine();
                        boolean sideBool = false;
                        if (side.equals("p")) {
                            sideBool = true;
                        }
                        data = new ArrayList();
                        data.add(argumentTopicUUID);
                        data.add(commentContent);
                        data.add(clientUsername);
                        data.add(String.valueOf(sideBool));
                        System.out.println(Arrays.toString(data.toArray()));

                        oos.writeObject(new Message(Message.MessageType.ADD_COMMENT, data));
                        oos.flush();
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "4":
                        System.out.println("Input username:");
                        username = sc.nextLine();
                        data.add(username);
                        System.out.println("Enter password");
                        pw = sc.nextLine();
                        data = new ArrayList();
                        data.add(pw);
                        oos.writeObject(new Message(Message.MessageType.DELETE_USER, data));
                        oos.flush();
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "5":
                        System.out.println("Input title:");
                        title = sc.nextLine();
                        System.out.println("Input content:");
                        content = sc.nextLine();
                        data = new ArrayList();
                        data.add(title);
                        data.add(content);
                        data.add(clientUsername);

                        String input = "";
                        while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                            System.out.println("Would you like to include an image? (y/n)");
                            input = sc.nextLine();
                        }

                        if (input.equalsIgnoreCase("y")) {
                            JFrame imageFrame = new JFrame();
                            JFileChooser chooser = new JFileChooser();
                            FileNameExtensionFilter filter = new FileNameExtensionFilter("Extension Filter",
                                    "jpg", "png");
                            chooser.setFileFilter(filter);
                            int returnVal = chooser.showOpenDialog(imageFrame);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                System.out.println("You chose to open this file: " +
                                        chooser.getSelectedFile().getName());
                                ImageIcon img = new ImageIcon(chooser.getSelectedFile().getPath());
                                data.add(img);
                                oos.writeObject(new Message(Message.MessageType.CREATE_ARGUMENT_TOPIC_IMAGE, data));
                                oos.flush();
                            } else {
                                oos.writeObject(new Message(Message.MessageType.CREATE_ARGUMENT_TOPIC, data));
                                oos.flush();
                            }
                        } else {
                            oos.writeObject(new Message(Message.MessageType.CREATE_ARGUMENT_TOPIC, data));
                            oos.flush();
                        }
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "7":
                        System.out.println("Input argument topic uuid:");
                        argumentTopicUUID = sc.nextLine() + ".txt";
                        data = new ArrayList();
                        data.add(argumentTopicUUID);
                        oos.writeObject(new Message(Message.MessageType.DELETE_ARGUMENT_TOPIC, data));
                        oos.flush();
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "8":
                        System.out.println("Enter friend username");
                        String friendUsername = sc.nextLine();
                        data = new ArrayList<>();
                        data.add(clientUsername);
                        data.add(friendUsername);
                        oos.writeObject(new Message(Message.MessageType.ADD_FRIEND, data));
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "9":
                        System.out.println("Enter friend username to remove");
                        friendUsername = sc.nextLine();
                        data = new ArrayList<>();
                        data.add(clientUsername);
                        data.add(friendUsername);
                        oos.writeObject(new Message(Message.MessageType.REMOVE_FRIEND, data));
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "10":
                        System.out.println("Enter username to block");
                        String blockedUsername = sc.nextLine();
                        data = new ArrayList();
                        data.add(clientUsername);
                        data.add(blockedUsername);
                        oos.writeObject(new Message(Message.MessageType.BLOCK_USER, data));
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "11":
                        System.out.println("Enter username to unblock");
                        String unblockedUsername = sc.nextLine();
                        data = new ArrayList();
                        data.add(clientUsername);
                        data.add(unblockedUsername);
                        oos.writeObject(new Message(Message.MessageType.UNBLOCK_USER, data));
                        response = (Message) ois.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "12":
                        System.out.println("Displaying all posts from friends");
                        data = new ArrayList();
                        data.add(clientUsername);
                        oos.writeObject(new Message(Message.MessageType.VIEW_NEWS_FEED, data));
                        response = (Message) ois.readObject();
                        ArrayList newsfeed = (ArrayList) response.getData().get(0);
                        System.out.println(newsfeed);
                        for (int i = 0; i < newsfeed.size(); i++) {
                            System.out.println((ArgumentTopic) newsfeed.get(i));
                        }
                        break;
                    case "13":
                        data.add("termination");
                        System.out.println("Goodbye!");
                        oos.writeObject(new Message(Message.MessageType.SAVE_DATA, data));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + message);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("networkio.Client Disconnected");
        }

    }

}
