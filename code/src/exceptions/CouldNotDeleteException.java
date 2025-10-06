package exceptions;

/**
 * Team Project Phase 1 -- Exceptions.CouldNotDeleteException
 *
 * This class defines a Exceptions.CouldNotDeleteException error.
 * Thrown when a called Object can not be deleted.
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, Dia Makaraju, CS 180
 *
 * @version November 3, 2024
 *
 */


public class CouldNotDeleteException extends Exception {
    public CouldNotDeleteException(String errorMessage) {
        super(errorMessage);

    }
}
