package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.DoesNotExistException
 *
 * This class defines a Exceptions.DoesNotExistException error.
 * Thrown when a call to any Object within the platform database does not exist.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */


public class DoesNotExistException extends Exception {
    public DoesNotExistException (String message) {
        super(message);
    }
}
