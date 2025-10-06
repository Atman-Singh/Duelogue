package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.InvalidPostException
 *
 * This class defines a Exceptions.InvalidPostException error.
 * Thrown when the deprecated.Post details are invalid.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */

public class InvalidPostException extends Exception {
    public InvalidPostException(String message) {
        super(message);
    }
}
