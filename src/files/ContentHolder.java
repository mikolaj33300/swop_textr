package files;

import java.io.IOException;

/**
 * An object that holds content
 */
public abstract class ContentHolder {

    /**
     * The array of bytes that defines which symbols are used to seperate a line on the system
     */
    protected byte[] lineSeparator;

    /**
     * The path that points towards where the content of this ContentHolder comes from
     */
    protected final String path;

    /**
     * Constructor of ContentHolder
     * Removes the reference to the lineSeperator
     */
    public ContentHolder(String path, byte[] lineSeparator) {
        this.lineSeparator = lineSeparator.clone();
        this.path = path;
    }

    /**
     * @return the line separator in use
     */
    public byte[] getLineSeparator() {
        return lineSeparator.clone();
    }

    /**
     * @return the path where the content comes from
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @return the content of ContentHolder in byte[] format
     * This function executes checks to ensure the returned content is valid and doesn't include invalid bytes
     */
    public abstract byte[] getContent() throws IOException, RuntimeException;

    /**
     * Saves the given fileContentr
     * @param fileContent the content to write away
     * @return 0 if the write is successful, 1 if it was not
     */
    abstract int save(byte[] fileContent);

    /**
     * @return a clone of ContentHolder, without the reference to it or its fields
     */
    abstract public ContentHolder clone();

    /**
     * @return true if this object equals to the given ContentHolder, false otherwise
     * The objects are equal if:
     * The given contentHolder has the same path as this ContentHolder.
     */
    abstract public boolean equals(ContentHolder contentHolder);
}
