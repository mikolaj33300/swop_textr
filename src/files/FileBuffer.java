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

    // deletes the line the cursor is on
    public void deleteLine(){
      int i, k = this.insertionPoint;
      while (i < this.byteContent.length && this.bytecontent[i++] != 10);// find next \n
      while (k && this.byteContent[k--] != 10);// find previous \n
      byte[] newContent = new byte[byteContent.length - i + k];

      for (int j = 0; j < k; j++)
	      newContent[j] = this.byteContent[j];
      for (int j = k; j < newContent.length; j++)
        newContent[j]=this.byteContent[j + i - k];
      this.byteContent = newContent;
    }

    // Prints the content of the file relative to the coordinates
    public void render(int startX, int startY, int width, int height) {
        String log = "";
        Terminal.leaveRawInputMode();

        String s = new String(byteContent);
        List<Integer> newLines = analyseContents(width, height);

        // Absolute values
        int xAdd = 0;
        int yAdd = 0;

        // Line separation length for bytes: 0d0a = 2 bytes, 0a = 1 byte.
        // When encountering a line separator, we skip 1 or 0 bytes, because termios will throw an error upon trying to print a line separator
        int lineAdd = this.file.getLineSeparator().length() / 2;

        for(int i = 0; i < byteContent.length; i++) {

            // We will always print one character per loop
            String character = s.substring(i, i+1);

            // We check if at index i for a line separator.
            if(newLines.contains(i)) {

                xAdd = 0;
                yAdd += 1;
                i += lineAdd; // Possibly skip 1 more byte incase of 0d0a
                log += " > Found line sep before max width at " + (i-1) + "\n";

                // When max width is reached, we find the next MANUAL line separator and starting printing from there.
            } else if(xAdd >= width) {

                // We search the newLines Map for the next line separator
                for(int separatorIndex : newLines)
                    if(separatorIndex >= i)
                        i = separatorIndex+1;

                log += " > new Line separator found at index " + i + "\n";

                yAdd++;
                xAdd = 0;

                log += " > Printing at: [" + (startX + xAdd) + ", " + (startY + yAdd) + "]\n";
                Terminal.printText(startY + yAdd, startX + xAdd, character);
                xAdd++;

                // No line separator found, we print text normally
            } else {

                log += "Printing at: [" + (startX + xAdd) + ", " + (startY + yAdd) + "]\n";
                Terminal.printText(startY + yAdd, startX + xAdd, character);
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
    private List<Integer> analyseContents(int width, int height) {

        // Read file content in bytes
        byte[] fileContent = this.byteContent;

        // Extract information about line seperators.
        Formatter formatterContent = new Formatter();
        for (byte b : fileContent) formatterContent.format("%02x", b);
        String fileContentFormatted = formatterContent.toString();

        // Determine the amount of lines this text has.
        List<Integer> newLinesOccurrence = new ArrayList<>();
        int counter = 0; // Keeps track of how many characters we have on one line.
        boolean found0d = false;

        // Loop over bytes in String form.
        for(int i = 0; i < fileContentFormatted.length()-1; i += 2) {

            // We take 2 characters at space i.
            String part = fileContentFormatted.substring(i, i+2);

            // If we have 0a, check if we had 0d before. We have a line separation eitherway.
            if(part.equals("0a")) {
                if(found0d) {
                    found0d = false;
                    newLinesOccurrence.add((i / 2) - 1);
                }
                else
                    newLinesOccurrence.add(i/2);

            } else
                found0d = false;


            // If this part is 0d, we have possibly found 0d0a. Set found0d true;
            if(part.equals("0d"))                 // Set counter to 0 again ; we are on a new line
                found0d = true;
            //System.out.println("! found 0d");


        }

        // Debug
        //for(Map.Entry<Integer, Boolean> e : newLinesOccurrence.entrySet())
        //    System.out.println("< i = " + e.getKey() + " --> l = " + e.getValue());

        return newLinesOccurrence;
    }

    /**
     * Creates the string representation of the byte[].
     * Used for finding line separators: they will be formatted as 0d0a or 0a
     * Note: every byte will be formatted to 2 string characters in ASCII.
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

    /**
     * Determines if a given byte[] is the same as this buffer's {@link FileBuffer#byteContent}
     */
    boolean contentsEqual(byte[] compare) {
        if(compare.length != byteContent.length) return false;
        for(int i = 0; i < compare.length; i++)
            if(compare[i] != byteContent[i]) return false;
        return true;
    }

    /**
     * Clones the byte array
     */
    byte[] cloneBytes() {
        byte[] copy = new byte[this.byteContent.length];
        for(int i = 0; i < byteContent.length; i++)
            copy[i] = byteContent[i];
        return copy;
    }

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath(), this.file.getLineSeparator());
        copy.dirty = this.dirty;
        copy.byteContent = this.cloneBytes();
        return copy;
    }

    /**
     * Checks if the given {@link FileBuffer} references the same {@link FileHolder}
     * and temporarily, if the content, and dirty boolean match.
     */
    public boolean equals(FileBuffer buffer) {
        return this.dirty == buffer.dirty && this.contentsEqual(buffer.byteContent) && this.file.getPath().equals(buffer.file.getPath());
    }

}
