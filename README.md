
# DUELOGUE

# How to Start
## Instructions Summary in Points

1. **Watch the Demo (Optional)**: View the project demo video [here](https://www.youtube.com/watch?v=KhL1OB76cQw).

2. **Starting the Program**:
    - Run `MainServer.java` (located in the `networkio` package inside the `src` folder).
    - Run `ClientGUI.java` to launch the client interface.
    - **Note**: Do not start `Client.java` (used for terminal-based testing in Phase 2).

3. **Simulating Multiple Users**:
    - Modify `ClientGUI.java`'s run configurations to start multiple clients.

4. **Creating a New Account**:
    - On the load-up screen, click **Create User** to register.
    - Username:
        - Case-sensitive.
        - Must not already exist.
    - Password requirements:
        - At least 8 characters long.
        - Must include:
            - One uppercase letter.
            - One lowercase letter.
            - One digit.
            - One special character.

5. **Login**:
    - After creating an account, login using the username and password.

6. **Exploring the Platform**:
    - On first login, view a **sample post**.
    - Create additional users and friend them to expand your network.
        - Example users with posts: **PeterGriffin**, **HomerSimpson**.
    - After friending them, click **View** to access your news feed.

7. **Interacting with Argument Topics**:
    - Post comments **for/against** the topic.
    - Upvote comments.
    - Delete your own comments.

8. **Creating Your Own Argument Topic**:
    - Navigate away from the news feed and click **Make Argument Topic**.
    - Provide a **title** and **content**.
    - View your posts under **View Your Profile** to interact with them.

9. **Known Issue**:
    - Comments added to the **Cons Panel** are mistakenly shown in the **Pros Panel**.
    - This functionality is under development.

10. **Enjoy the Platform**: Explore and engage in the Duelogue experience!

# All Classes and Interfaces
```
TeamProjectCS180000/
├── code/
├── data/
│   ├── Posts/
│   ├── Users/
│   ├── allPosts.txt
│   └── allUsers.txt
└── src/
    ├── database/
    │   ├── ArgumentTopic.java
    │   ├── ArgumentTopicInterface.java
    │   ├── Comment.java
    │   ├── CommentInterface.java
    │   ├── Database.java
    │   ├── DatabaseInterface.java
    │   ├── User.java
    │   └── UserInterface.java
    │
    ├── exceptions/
    │   ├── CouldNotAddCommentException.java
    │   ├── CouldNotDeleteException.java
    │   ├── DoesNotExistException.java
    │   ├── InvalidCredentialsException.java
    │   ├── InvalidPostException.java
    │   └── InvalidUserException.java
    │
    ├── gui/
    │   ├── ArgumentTopicPage.java
    │   ├── ArgumentTopicPageInterface.java
    │   ├── CreateUserInterface.java
    │   ├── CreateUserPage.java
    │   ├── DeleteUserInterface.java
    │   ├── DeleteUserPage.java
    │   ├── FeedPage.java
    │   ├── FeedPageInterface.java
    │   ├── LoginInterface.java
    │   ├── LoginPage.java
    │   ├── MakeArgumentTopicInterface.java
    │   ├── MakeArgumentTopicPage.java
    │   ├── UserPage.java
    │   └── UserPageInterface.java
    │
    ├── networkio/
    │   ├── Client.java
    │   ├── ClientGUI.java
    │   ├── ClientInterface.java
    │   ├── MainServer.java
    │   ├── Message.java
    │   ├── MessageInterface.java
    │   └── ServerInterface.java
    │
    └── unittests/
        ├── ArgumentTopicTest.java
        ├── ClientTest.java
        ├── CommentTest.java
        ├── DatabaseTest.java
        ├── MainServerTest.java
        └── UserTest.java
```
# Class ArguementTopic.java

## **Field Summary**

private ArrayList\<database.Comment\> comments \- The list of comments on this argument topic.  
private Image image \- An image associated with this argument topic.  
private boolean updated \- Indicates whether the topic has been updated.  
private String title \- The title of the argument topic.  
private String content \- The content of the argument topic.  
private int engagement \- The total engagement score of the argument topic, based on upvotes from comments.  
private database.User author \- The author of the argument topic.  
private int proEngagement \- The engagement score from pro-side comments.  
private int antiEngagement \- The engagement score from anti-side comments.  
private String fileCode \- A unique file code for the topic, generated as a UUID string with .txt extension.  
private String imagepath \- The file path to the image associated with this argument topic.


## **Constructor Summary**

database.ArgumentTopic(String title, String content, database.User author) \- Initializes an database.ArgumentTopic with the specified title, content, and author, and sets initial values for engagement, updated, pro and anti engagement scores, and generates a unique fileCode.

## **Method Summary**

int getEngagement() \- Calculates and returns the total engagement score for this topic, summing upvotes from all comments.  
String getImagepath() \- Returns the file path to the image associated with this argument topic.  
void getEngagementSides() \- Separately calculates the pro and anti engagement scores by analyzing comment sides and upvotes.  
ArrayList\<database.Comment\> getComments() \- Returns the list of comments on this argument topic.  
void appendComment(database.Comment comment) \- Adds a comment to the list of comments for this argument topic.  
String getTitle() \- Returns the title of this argument topic.  
String getContent() \- Returns the content of this argument topic.  
String getFileCode() \- Returns the unique file code of this argument topic.  
database.User getAuthor() \- Returns the author of this argument topic.  
int getAntiEngagement() \- Returns the anti engagement score.  
int getProEngagement() \- Returns the pro engagement score.  
void setEngagement(int engagement) \- Sets the engagement score for this argument topic.  
boolean equals(Object o) \- Checks if this database.ArgumentTopic is equal to another based on title, content, and author.  
String toString() \- Returns a formatted string representation of the argument topic, including details of all comments.

# Class ArguementTopicInterface.java

## **Method Summary**

int getEngagement() \- Retrieves the total engagement score of the argument topic.  
String getImagepath() \- Retrieves the file path to the image associated with this argument topic.  
ArrayList\<database.Comment\> getComments() \- Retrieves the list of comments on this argument topic.  
String getTitle() \- Retrieves the title of this argument topic.  
String getContent() \- Retrieves the content of this argument topic.  
String getFileCode() \- Retrieves the unique file code of this argument topic.  
database.User getAuthor() \- Retrieves the author of this argument topic.

void getEngagementSides() \- Separately calculates the pro and anti engagement scores, categorizing comments based on their stance.  
void appendComment(database.Comment comment) \- Adds a new comment to the list of comments for this argument topic.

boolean equals(Object o) \- Checks if this database.ArgumentTopic is equal to another by comparing title, content, and author.  
String toString() \- Returns a formatted string representation of the argument topic, including all comments.

# Class database.User.java

## **Field Summary**

private String username \- The username of the user.  
private String password \- The password of the user.  
private int karma \- The karma score of the user, calculated from engagement on topics and comments.  
private ArrayList\<String\> friendList \- List of the user's friends.  
private ArrayList\<database.ArgumentTopic\> argumentTopicHistory \- History of argument topics created or engaged in by the user.  
private ArrayList\<database.Comment\> commentHistory \- History of comments made by the user.  
private ArrayList\<String\> blockedUsers \- List of usernames blocked by the user.  
private String profilePhotoPath \- Path to the user's profile photo.  
Constructor Summary  
database.User(String username, String password) \- Initializes a database.User object with the specified username and password. Also initializes the friend list, argument topic history, comment history, and blocked users list. Randomly selects a default profile photo from a predefined set.

## **Method Summary**

ArrayList\<database.ArgumentTopic\> getArgumentTopicHistory() \- Returns the user's history of argument topics.  
ArrayList\<String\> getFriendlist() \- Returns the user's friend list.  
int getKarma() \- Calculates and returns the user's karma score based on the engagement of their topics and upvotes on comments.  
String getUsername() \- Returns the username of the user.  
ArrayList\<String\> getBlockedUsers() \- Returns the list of users blocked by this user.  
String getPassword() \- Returns the user's password.  
String getProfilePhotopath() \- Returns the file path of the user's profile photo.  
ImageIcon getProfilePhoto() \- Returns the user's profile photo as an ImageIcon object.  
ArrayList\<database.Comment\> getCommentHistory() \- Returns the user's comment history.

void setUsername(String username) \- Sets the username of the user.  
void setProfilePhoto(String profilePhotoPath) \- Sets the file path for the user's profile photo.

boolean equals(Object o) \- Checks if this database.User object is equal to another by comparing their usernames and passwords.  
String toString() \- Returns a formatted string representation of the user, including their username, password, and profile photo path.

# Class database.UserInterface.java

## **Method Summary**

ArrayList\<database.ArgumentTopic\> getArgumentTopicHistory() \- Retrieves the list of argument topics the user has created or engaged in.  
ArrayList\<String\> getFriendlist() \- Retrieves the list of friends associated with the user.  
int getKarma() \- Calculates and returns the karma score for the user based on their engagement in argument topics and upvotes on comments.  
String getUsername() \- Retrieves the username of the user.  
ArrayList\<String\> getBlockedUsers() \- Retrieves the list of usernames that the user has blocked.  
String getPassword() \- Retrieves the password of the user.  
String getProfilePhotopath() \- Retrieves the file path to the user's profile photo.  
ImageIcon getProfilePhoto() \- Retrieves the user's profile photo as an ImageIcon object.

void setUsername(String username) \- Updates the user's username.  
void setProfilePhoto(String profilePhotoPath) \- Updates the file path for the user's profile photo.

boolean equals(Object o) \- Compares this database.User object to another for equality based on the username and password.  
String toString() \- Returns a string representation of the user, including relevant user information like username and profile photo path.

# Class database.Comment.java

## **Field Summary**

private boolean side \- Indicates the side of the comment (e.g., pro or con).  
private int upvotes \- The number of upvotes the comment has received.  
private int downvotes \- The number of downvotes the comment has received.  
private String content \- The text content of the comment.  
private database.User author \- The user who authored the comment.

## **Constructor Summary**

database.Comment(boolean side, String content, database.User author) \- Initializes a database.Comment object with the specified side, content, and author. Sets the initial upvotes to 1 and downvotes to 0\.

## **Method Summary**

int getUpvotes() \- Returns the effective score of the comment by calculating upvotes minus downvotes.  
boolean getSide() \- Returns the side of the comment (e.g., pro or con).  
String getContent() \- Returns the content of the comment.  
database.User getAuthor() \- Returns the user who authored the comment.

void incrementUpvotes() \- Increases the upvote count by one.  
void incrementDownvotes() \- Increases the downvote count by one.

boolean equals(Object o) \- Checks if this database.Comment object is equal to another by comparing their side, content, and author.  
String toString() \- Returns a formatted string representation of the comment, including the content, author's username, and side.

# Class database.CommentInterface.java

## **Method Summary**

int getUpvotes() \- Retrieves the current number of effective upvotes for the comment, calculated as upvotes minus downvotes.  
boolean getSide() \- Retrieves the side of the comment (e.g., pro or con).  
String getContent() \- Retrieves the content of the comment.  
database.User getAuthor() \- Retrieves the user who authored the comment.

void incrementUpvotes() \- Increases the count of upvotes for the comment by one.  
void incrementDownvotes() \- Increases the count of downvotes for the comment by one.

boolean equals(Object o) \- Compares this database.Comment object with another for equality based on their side, content, and author.  
String toString() \- Returns a string representation of the comment, formatted to include its content, the author's username, and its side.

# Class networkio.Client.java

## **Field Summary**

private static final String SERVER\_ADDRESS \- The server's address, defaulted to "localhost".  
private static final int SERVER\_PORT \- The server's port number, defaulted to 4201\.

## **Constructor Summary**

networkio.Client() \- Default constructor for the networkio.Client class.

## **Method Summary**

public static void main(String\[\] args) \- Entry point for the client application, handles the socket connection, user input, and server communication.  
private boolean createUser(Scanner sc, ObjectOutputStream oos, ObjectInputStream ois) \- Creates a new user by taking input and communicating with the server.  
private boolean loginUser(Scanner sc, ObjectOutputStream oos, ObjectInputStream ois) \- Handles user login by validating credentials against the server.  
private void handleMenuOptions(Scanner sc, ObjectOutputStream oos, ObjectInputStream ois) \- Processes menu options selected by the user and sends the corresponding requests to the server.  
private void sendMessage(ObjectOutputStream oos, networkio.Message message) \- Helper method to send a networkio.Message object to the server.  
private networkio.Message receiveMessage(ObjectInputStream ois) \- Helper method to receive a networkio.Message object from the server.

# Class database.Database.java

## **Field Summary**

ArrayList\<database.User\> users \- List of all users in the database   
ArrayList\<database.ArgumentTopic\> argumentTopics \- List of all argument topics String userOut \- Output destination for user data   
String argumentTopicIn \- Input source for argument topics   
ArrayList\<Image\> Images \- List of images associated with arguments static final   
Object o \- Object used for synchronizing access   
static boolean\[\] locks \- Array to manage file access locks   
static final String PASSWORD\_PATTERN \- Regex pattern for password validation static final   
String SEPARATOR \- Separator used to organize data in files

## **Constructor Summary**

database.Database(ArrayList\<String\> userIn) \- Initializes database with user input files and loads data database.Database() \- Creates empty database with no initial data

## **Method Summary**

database.Database(ArrayList\<String\> userIn) \- Initializes database with user input files and loads data   
boolean createUser(String username, String pw) \- Creates new user with given credentials  
boolean deleteUser(String username, String pw) \- Deletes user if credentials match  
database.User getUser(String username) \- Retrieves user by username  
boolean verifyLogin(String userName, String pw) \- Verifies user login credentials  
boolean addFriend(String addingUserName, String addedUserName) \- Adds user to friend list  
boolean removeFriend(String removingUser, String removedUser) \- Removes user from friend list  
boolean blockUser(String blockerName, String blockedName) \- Adds user to block list  
boolean unBlockUser(String blockerName, String blockedName) \- Removes user from block list  
boolean createArgumentTopic(String title, String content, String authorUsername) \- Creates new argument topic  
boolean deleteArgumentTopic(database.ArgumentTopic topic) \- Deletes specified argument topic  
database.ArgumentTopic getArgumentTopic(String uuid) \- Retrieves argument topic by UUID  
boolean addComment(database.ArgumentTopic topic, database.Comment comment) \- Adds comment to argument topic  
database.Comment getComment(database.ArgumentTopic argumentTopic, String commentUUID) \- Retrieves comment by UUID  
boolean deleteComment(database.ArgumentTopic topic, database.Comment comment) \- Deletes specified comment  
void loadDatabase(ArrayList\<String\> userIn) \- Loads user data from input files  
boolean saveData() \- Saves all data to files  
database.ArgumentTopic processArgumentTopicFile(String filename, database.User user) \- Processes argument topic file data  
void writeToFile(String data, String directory, String fileName, boolean append) \- Writes data to specified file  
void removeStringFromFile(String filePath, String target) \- Removes specified string from file  
ArrayList\<database.ArgumentTopic\> loadfeed(database.User user) \- Loads personalized feed for user  
boolean validatePassword(String password) \- Validates password against requirements  
ArrayList\<database.ArgumentTopic\> getArgumentTopics() \- Returns all argument topics  
ArrayList\<database.User\> getUsers() \- Returns all users

## 	

# Class database.DatabaseInterface.java

## **Method Summary**

void loadDatabase(ArrayList userIn, boolean\[\] locks) \- Loads the database using the given user data and lock states.  
database.ArgumentTopic processArgumentTopicFile(String filename, database.User user) \- Processes a file to create an database.ArgumentTopic associated with the specified user.  
boolean saveData() \- Saves all data to the persistent storage.  
boolean deleteComment(database.ArgumentTopic topic, database.Comment comment) \- Deletes the specified comment from the given database.ArgumentTopic.  
boolean createUser(String username, String pw) \- Creates a new user with the provided username and password.  
boolean addComment(database.ArgumentTopic topic, database.Comment comment) \- Adds a comment to the specified database.ArgumentTopic.  
database.User getUser(String username) \- Retrieves the database.User object corresponding to the provided username.  
boolean validatePassword(String password) \- Validates the given password according to the system's criteria.  
void writeToFile(String data, String directory, String fileName, boolean append) \- Writes data to a file in the specified directory with an option to append.  
boolean deleteUser(String username, String pw) \- Deletes a user with the given credentials.  
boolean createArgumentTopic(String title, String content, String authorUsername) \- Creates an database.ArgumentTopic with the provided details.  
boolean createArgumentTopic(String title, String content, String authorUsername, ImageIcon image) \- Creates an database.ArgumentTopic with the provided details and an associated image.  
database.ArgumentTopic getArgumentTopic(String uuid) \- Retrieves the database.ArgumentTopic corresponding to the provided unique identifier.  
boolean deleteArgumentTopic(database.ArgumentTopic topic) \- Deletes the specified database.ArgumentTopic.  
boolean addFriend(String addingUsername, String addedUserName) \- Adds a friend connection between the given users.  
boolean removeFriend(String removingUser, String removedUser) \- Removes a friend connection between the given users.  
boolean unBlockUser(String blockerUsername, String blockedUsername) \- Unblocks the specified user.  
void removeStringFromFile(String filePath, String target) \- Removes occurrences of a string from a specified file.  
ArrayList loadFeed(database.User user) \- Loads the feed of posts and topics for the specified user.


# Class Mainserver.java

## **Field Summary**

static Socket socket \- The socket used for client-server communication.  
static ServerSocket serverSocket \- The server socket listening for incoming connections.  
static database.Database db \- The database instance used to manage application data.

## **Constructor Summary**

networkio.MainServer() \- Initializes the server by creating a new database.Database instance.

## **Method Summary**

void run() \- Handles client-server communication. Processes incoming messages based on their types and invokes the corresponding database operations. Sends appropriate responses to the client.

static void main(String\[\] args) throws IOException, InterruptedException   
\- Entry point of the server application. Sets up the server socket, loads user data from files into the database, and starts threads to handle client connections.

## 

# PHASE 2 (IMP) \- Testing Instruction Summary

**NOTE:** networkio.MainServer.java is built for the UI.  
The UI will be able to obtain the UUID (identity code for the database.ArgumentTopic) from the UI itself.

While testing Network I/O, the entire test is conducted using the Scanner object. Because of this, you will need to obtain the UUID of the created database.ArgumentTopic to test the following functions:  
ADD\_COMMENT, DELETE\_COMMENT, and DELETE\_ARGUMENTTOPIC.

The method to obtain the UUID is written below

**NOTE2:** As networkio.MainServer.java is built for the UI, there is no possibility for the UI to provide invalid inputs. Functions will be loaded from the code itself and will not be typed in by the client. Therefore, there is no need to check for invalid inputs, and they have not been handled.


## 

1. ## **To Start the Server**

    1. ### Run networkio.MainServer.java

    2. ### Run networkio.Client.java as many times as users you want to be running simultaneously.(to be able to run multiple clients, go to the networkio.Client.java file. Right click and go to “More Run/Debug” –\> select “Modify Run configurations” –\> “Modify options” —\> 	“allow multiple instances”

2. ## **Create a user or login**

    1. ### Either create a user by entering 1

       Enter a **unique username** that does not already exist in the database.

       Enter a **valid password** that meets the following criteria:

* At least 8 characters long.
* Contains at least one symbol.
* Includes an uppercase letter, a lowercase letter, and a digit.

  	The system will prompt you until the username and password are accepted.

2. ### Or login by entering 2

   Enter a **valid username-password pair** (the pair must exist in the database).

   The system will continue prompting you until the correct credentials are provided.

**Example:**  
**Input username:**  
(username123)  
**Input password:**  
(Helios@75)

3. ## **Delete database.Comment (Option 1\)**

### **Note: Make a comment using option 3 before using option 1**

1. ### Provide the **argument topic UUID** associated with the comment

### ![][image1]

The long file code followed by the .txt is the example of argumentTopic UUID when you view a news feed.

2. ### Enter the **UUID of the comment** you wish to delete.

    3. ### The system will process and attempt to delete the specified comment.

### **Example:**

### **Input argument topic uuid:**

### (Argument Topic UUID)

**Input comment uuid:**  
(database.Comment UUID)

## 

4. ## **Get database.User Information (Option 2):**

    1. ### Enter the username of the user you want to retrieve details for.

    2. ### The system will fetch and display information about the specified user.

### **Example:**

### **Input username:**

### (username123)

### 	

5. ## **Add a database.Comment (Option 3):**

    1. ### Provide the argument topic UUID where you want to add a comment.

    2. ### Enter the content of your comment.

    3. ### Indicate your side (use p for pro or a for against).

    4. ### Provide your username to associate with the comment.

    5. ### The system will save your comment and associate it with the specified topic.
Ignore indexoutofbounds error because file changes are shown.
### **Example:**

### **Input argument topic uuid:**

### (Argument Topic UUID)

**Input comment content:**   
(This is a test comment)  
**Enter your side: p for pro and a for against**  
(p) OR (a)

## 

6. ## **Delete a database.User (Option 4):**

    1. ### Enter the username of the account you wish to delete.

    2. ### Provide the password for verification.

    3. ### The system will delete the user if the credentials are correct.

### **Example:**

**Input username:**  
(username123)  
**Input password:**  
(Helios@75)

## 

7. ## **Create an Argument Topic (Option 5):**

    1. ### Enter a title for the argument topic.

    2. ### Provide content for the topic.

    3. ### Enter your username to associate with the topic.

    4. ### Indicate whether to include an image:

        * ### If yes, select an image file using the file chooser.

        * ### If no, the topic will be created without an image.

   ### The system will create the topic and save it to the database.

**Example:**  
**Input Title:**  
(Argument Topic Test)  
**Input Content:**  
(This is a test this is not a real Argument Topic)  
**Would you like to include an image? (y/n)**  
(y)/(n)  
IF Y  
Follow JFileChooser

## 

8. ## **Delete an Argument Topic (Option 7):**

    1. ### Enter the UUID of the argument topic you want to delete.

    2. ### The system will delete the specified topic.

**Example:**  
**Input argument topic uuid:**  
(Argument Topic UUID)  
Enter friend username

### 

9. ## **Add a Friend (Option 8):**

Note: For this function to work, run client.java again (in step u)

1. ### Provide your username.

    2. ### Enter the username of the friend you want to add.

    3. ### The system will add the specified friend to your friend list.

**Example:**  
**Enter friend username**  
(username456)

## 

10. ## **Remove a Friend (Option 9):**

    1. ### Provide your username.

    2. ### Enter the username of the friend you want to remove.

    3. ### The system will remove the friend from your list

**Example:**  
**Enter friend username to remove**  
(username456).

## 

11. ## **Block a database.User (Option 10):**

    1. ### Enter the username of the user you want to block.

    2. ### The system will block the specified user, preventing further interaction.

**Example:**  
**Enter username to block**  
(username456)

12. ## **Unblock a database.User (Option 11):**

    1. ### Provide your username.

    2. ### Enter the username of the user you want to unblock.

    3. ### The system will unblock the specified user.

**Example:**  
**Enter username to unblock**  
(username456)


13. ## **View News Feed (Option 12):**

    1. ### Enter your username.

    2. ### The system will display your news feed, showing updates and interactions from your network.

**Example:**  
**Displaying all posts from friends**

if friend list empty restart the program.

## 

14. ## **Quit (Option 13):**

    1. ### Enter 13 to terminate the program.

    2. ### The system will send a termination message and disconnect the client.

**Example:**  
**Goodbye\!**

## 

# Class networkio.Message.java

## **Field Summary**

private MessageType messageType \- The type of the message, defined by the MessageType enum.  
private String message \- The textual content of the message.  
private ArrayList data \- Alternative data associated with the message if not strictly a single String.

## **Constructor Summary**

networkio.Message(MessageType messageType, String message) \- Constructs a networkio.Message with the specified type and textual content.  
networkio.Message(MessageType messageType, ArrayList data) \- Constructs a networkio.Message with the specified type and associated data.

## **Method Summary**

MessageType getMessageType() \- Returns the type of the message.  
String getMessage() \- Returns the textual content of the message.  
ArrayList getData() \- Returns the data associated with the message.

# 

# 

# exceptions

### **CouldNotAddCommentException**

* This exception is thrown when there is an error adding a comment, possibly due to validation failures or issues with the comment storage mechanism.

## 

### **CouldNotDeleteException**

* This exception is thrown when there is an error deleting an object, such as a comment or post, due to various reasons (e.g., the object does not exist).

## 

### **InvalidCredentialsException**

* This exception is thrown when a user's credentials (username/password) are invalid, typically during a login attempt.

## 

### **InvalidPostException**

* This exception is thrown when an attempt to create or manipulate a post is made with invalid data, such as incorrect formatting or missing required fields.

## 

### **InvalidUserException**

* This exception is thrown when an operation is attempted on a user that is not valid, which could include cases like non-existent users or users that are blocked.

## 

### **DoesNotExistException**

* This exception is thrown when an attempt is made to access or manipulate an object that does not exist, such as a comment or user.

[image1]: <data:image/png;base64,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>
JavaDocAndTestingInstructions.md
Displaying JavaDocAndTestingInstructions.md.
