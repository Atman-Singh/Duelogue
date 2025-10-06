package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.InvalidCredentialsException
 *
 * This class defines a Exceptions.InvalidCredentialsException error.
 * Thrown when a database.User's login credentials are invalid.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */


public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}
