package gui;

import database.ArgumentTopic;
import database.Comment;
import networkio.Message;


/**
 * Team Project Phase 3 -- ArgumentTopicPageInterface
 * This interface outlines the methods that should be implemented for a ArgumentTopicPage object
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

public interface ArgumentTopicPageInterface {
    Message postComment(ArgumentTopic topic, boolean side, String content, String author);
    Message deleteComment(ArgumentTopic topic, String commentCode);
    Message upVoteComment(Comment comment, String user, ArgumentTopic topic);
    Message downVoteComment(Comment comment, String user, ArgumentTopic topic);
}
