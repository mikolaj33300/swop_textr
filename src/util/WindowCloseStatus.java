package util;

/**
 * Enum for the status of the closure of a window
 * UNSAFE_CLOSE: the window was closed unsafely (not saved before closing)
 * CLOSED_SUCCESFULLY: the window was closed successfully
 * LAST_WINDOW_CLOSED: the last window was closed in a DisplayFacade
 */
public enum WindowCloseStatus {
    UNSAFE_CLOSE, CLOSED_SUCCESSFULLY, LAST_WINDOW_CLOSED;
}
