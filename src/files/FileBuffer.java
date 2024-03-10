package files;

import io.github.btj.termios.Terminal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileBuffer {

    /**
     * File reference
     */
    private FileHolder file;

    /**
     * Determines if buffer has been modified
     */
    private boolean dirty = false;

    /**
     * Holds the 'in memory' byte data of the file.
     * The amount of bytes equals the amount of characters in ASCII
     */
    private byte[] byteContent;

    private int insertionPoint;

    /**
     * Creates FileBuffer object with given path;
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     */
    public FileBuffer(String path, String lineSeparator) {
        this.file = new FileHolder(path, lineSeparator);
        this.byteContent = this.file.getContent();
        this.insertionPoint = 0;
    }

    // Implementation
    public void enterInsertionPoint() {
        insert(System.lineSeparator().getBytes());
    }

    // Prints the content of the file relative to the coordinates
    public void render(int topRightX, int topRightY, int width, int height) {
        String log = "";

        String s = new String(byteContent);
        Map<Integer, Boolean> newLines = analyseContents(width, height);

        // Relative positions to start printing from
        int startX = topRightX;
        int startY = topRightY;

        // Absolute values
        int xAdd = 0;
        int yAdd = 0;

        // Line separation length for bytes: 0d0a = 2 bytes, 0a = 1 byte.
        int lineAdd = this.file.getLineSeparator().length() / 2;

        for(int i = 0; i < byteContent.length; i++) {

            if(newLines.containsKey(i)) {
                // Determine if the newline is by line separator or max width reached
                boolean key = false;
                for(Map.Entry<Integer, Boolean> keys : newLines.entrySet())
                    if(keys.getKey().equals(i))
                        key = keys.getValue();

                yAdd += 1;                  // Add new line
                if(key) { // If we have a key, we just skip without printing the next character
                    xAdd = 0;
                    i += lineAdd;           // Possibly skip 1 more byte
                } else { // If the newline is due to max width reached, we print the next character
                    xAdd = 0;
                    Terminal.printText(startY + yAdd, startX, s.substring(i, i+1));
                    xAdd = 1;
                }

            } else {

                Terminal.printText(startY + yAdd, startX + xAdd, s.substring(i, i+1));
                xAdd++;

            }

        }

        System.out.println("\n" + log);

    }

    /**
     * Updates the content of the FileBuffer
     */
    public void write(String updatedContents) {
        insert(updatedContents.getBytes());
        dirty = true;
    }

    /**
     * Saves the buffer contents to disk
     */
    public final void save() {
        if (!dirty) return;
        this.file.save(this.byteContent);
        this.dirty = false;
    }

    /**
     * Inserts the byte values.
     */
    private void insert(byte[] data) {
        byte[] newContent = new byte[byteContent.length + data.length];
        for(int i = 0; i < byteContent.length + data.length; i++) {
            if(i < insertionPoint)
                newContent[i] = byteContent[i];
            else if(i >= insertionPoint && i < insertionPoint + data.length)
                newContent[i] = data[i-insertionPoint];
            else newContent[i] = byteContent[i - data.length];
        }
        byteContent = newContent;
    }

    /**
     * Called from somewhere that knows the dimensions of a Leaf Layout.
     */
    public int moveInsertionPoint(int height, int width, char code) {
        Map<Integer, Boolean> amountLines = analyseContents(width, height);

        switch(code) {
            // Right
            case 'C':
                insertionPoint++;
                break;
            // Left
            case 'D':
                insertionPoint--;
                break;
            // Up
            case 'A':
                // Case 1: text fits on one line in buffer


                // Down
            case 'B':
                insertionPoint += width;
        }
        return 0;
    }

    /**
     * Returns a Map<Integer, Boolean> which indicates at which integer a line separator should be printed.
     * The byte map contains "0d0a" or "0a" as line separators. Other line separations are found by checking
     * the dimension of the window.
     * Used for rendering.
     */
    private Map<Integer, Boolean> analyseContents(int width, int height) {

        // Read file content in bytes
        byte[] fileContent = this.byteContent;

        // Extract information about line seperators.
        Formatter formatterContent = new Formatter();
        for (byte b : fileContent) formatterContent.format("%02x", b);
        String fileContentFormatted = formatterContent.toString();

        // Determine the amount of lines this text has.
        Map<Integer, Boolean> newLinesOccurrence = new HashMap<>();
        int counter = 0; // Keeps track of how many characters we have on one line.
        boolean found0d = false;

        for(int i = 0; i < fileContentFormatted.length()-1; i += 2) {
            counter++;
            String part = fileContentFormatted.substring(i, i+2);
            if(part.equals("0d")) {
                counter = 0;                       // Set counter to 0 again ; we are on a new line
                found0d = true;
            }
            if(part.equals("0a")) {
                if(found0d) {
                    found0d = false;
                    newLinesOccurrence.put((i / 2) - 1, true);
                }
                else {
                    newLinesOccurrence.put(i/2, true);
                }
                counter = 0;
            }
            if(counter == width) {
                counter = 0;
                newLinesOccurrence.put(i/2, false);
            }
        }

        return newLinesOccurrence;
    }

    /**
     * Creates the string representation of the byte[].
     * Used for finding line separators
     */
    private String formatBytes(byte[] bytes) {
        Formatter formatter = new Formatter();
        for(byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }

    // Test Methods

    /**
     * Returns the FileHolder object
     */
    FileHolder getFileHolder() {
        return this.file;
    }

    /**
     * Returns copy of this buffers' content.
     */
    byte[] getContent() {
        return byteContent.clone();
    }

    /**
     * Determines if the buffer is empty
     */
    boolean isDirty() {
        return this.dirty;
    }

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath(), this.file.getLineSeparator());
        copy.dirty = this.dirty;
        //copy.content = new String(this.content);
        return copy;
    }

    /**
     * Checks if the given {@link FileBuffer} references the same {@link FileHolder}
     * and temporarily, if the content, and dirty boolean match.
     */
    public boolean equals(FileBuffer buffer) {
        return this.dirty == buffer.dirty && new String(this.byteContent).equals(new String(buffer.byteContent)) && this.file.getPath().equals(buffer.file.getPath());
    }

}
