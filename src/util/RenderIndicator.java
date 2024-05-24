package util;

/**
 * Enum that denotes which part of the contents of textr has changed
 * FULL: the whole text has changed
 * WINDOW: only a window has changed
 * LINE: only a line has changed
 * CURSOR: only the cursor (postion) has changed
 * NONE: nothing has changed
 */
public enum RenderIndicator {
    FULL, WINDOW, LINE, CURSOR, NONE;
}
