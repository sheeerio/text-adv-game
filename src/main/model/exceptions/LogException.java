package model.exceptions;

/**
 * Represents the exception that can occur when
 * printing the event log.
 */
// LogException contains information about the logException exception
public class LogException extends Exception {
    // constructs exception string to be thrown
    public LogException() {
        super("Error printing log");
    }

    // constructs exception that throws the given message
    public LogException(String msg) {
        super(msg);
    }
}
