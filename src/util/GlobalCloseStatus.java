package util;

/**
 * Enum for the status of the closure of the whole textr program
 * CLOSED_SUCCESFULLY: the program was closed successfully
 * DIRTY_CLOSE_PROMPT: the program was closed unsafely (not saved before closing), use still has to answer a prompt
 * CLOSED_ONE_DISPLAY: one display was closed
 * CLOSED_ALL_DISPLAYS: all displays were closed, textr can be closed safely
 */
public enum GlobalCloseStatus {
    CLOSED_SUCCESFULLY, DIRTY_CLOSE_PROMPT, CLOSED_ONE_DISPLAY, CLOSED_ALL_DISPLAYS
}
