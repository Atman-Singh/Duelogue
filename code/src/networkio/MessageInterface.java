package networkio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Team Project Phase 2 -- networkio.Message Interface
 *
 * This interface outlines the methods that should be implemented for any networkio.Message object,
 * including retrieving message type, message content, and data associated with the message.
 * The interface serves as a contract for different types of message classes.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version November 12, 2024
 *
 */

public interface MessageInterface extends Serializable {

    /**
     * Retrieves the message type of this message.
     *
     * @return MessageType representing the type of this message
     */
    Message.MessageType getMessageType();

    /**
     * Retrieves the content of the message.
     *
     * @return String representing the content of this message
     */
    String getMessage();

    /**
     * Retrieves the data associated with this message.
     *
     * @return ArrayList containing the data for this message
     */
    ArrayList getData();
}
