package networkio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Team Project Phase 2 -- networkio.Message
 *
 * This class outlines a networkio.Message object to be sent back and forth between a client and the server via
 * ObjectOutputStream and ObjectInputStream, allowing the server to perform different functions depending on
 * the state (messageType) of the message
 *
 * @author Raghav Gangatirkar, Atman Singh,
 * Aditya Pachpande, S 180
 *
 * @version November 12, 2024
 *
 */

public class Message implements Serializable, MessageInterface {
    public enum MessageType {
        DELETE_COMMENT, CREATE_USER, LOGIN, GET_USER, USER, ADD_COMMENT, DELETE_USER, CREATE_ARGUMENT_TOPIC,
        CREATE_ARGUMENT_TOPIC_IMAGE, GET_ARGUMENT_TOPIC, ARGUMENT_TOPIC, DELETE_ARGUMENT_TOPIC, ADD_FRIEND, SAVE_DATA,
        REMOVE_FRIEND, BLOCK_USER, USER_LIST, UNBLOCK_USER, VIEW_NEWS_FEED, NEWS_FEED, ERROR, CHANGE_UPVOTE_STATUS,
        GET_RANDOM_USERS, GET_SEARCH_RESULTS, SUCCESS, UPVOTE, DOWNVOTE
    }

    private MessageType messageType;
    private String message;
    private ArrayList data;

    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public Message(MessageType messageType, ArrayList data) {
        this.messageType = messageType;
        this.data = data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList getData() { return data; }

}
