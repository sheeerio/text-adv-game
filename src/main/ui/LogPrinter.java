package ui;

import model.EventLog;
import model.exceptions.LogException;

/**
 * Defines behaviours that event log printers must support.
 */
// this interface contains information about logprinters
public interface LogPrinter {
    /**
     * Prints the log
     * @param el  the event log to be printed
     * @throws LogException when printing fails for any reason
     */
    // EFFECTS: prints the logs to the console
    void printLog(EventLog el) throws LogException;
}

