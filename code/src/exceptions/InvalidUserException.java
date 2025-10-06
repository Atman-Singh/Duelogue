package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.InvalidUserException
 *
 * This class defines a Exceptions.InvalidUserException error.
 * Thrown when a database.User object is invalid.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */


public class InvalidUserException extends DoesNotExistException {
    public InvalidUserException(String message) {
        super(message);
    }
}
