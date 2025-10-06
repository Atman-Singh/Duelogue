package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.CouldNotAddCommentException
 *
 * This class defines a Exceptions.CouldNotAddCommentException error.
 * Thrown when a called database.Comment object can't be added to an database.ArgumentTopic.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */


public class CouldNotAddCommentException extends Exception {
    public CouldNotAddCommentException(String message) {
        super(message);
    }
}
