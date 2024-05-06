package files;



import java.io.IOException;
import java.util.Arrays;

/**
 * An object that holds content in a String format
 */
public class StringHolder extends ContentHolder {

    /**
     * Tne String that contains the contents of this StringHolder
     */
    private String containedText;

    /**
     * Constructor of StringHolder
     * Removes the reference to the lineSeperator
     */
    public StringHolder(String path, byte[] lineSeparator, String content) {
        super(path, lineSeparator);
        this.containedText = content;
    }

    /**
     * @return the containedText, without any validity checks
     */
    public String getContainedText() {
        return this.containedText;
    }

    /**
     * @return the content of StringHolder in byte[] format
     * This function executes checks to ensure the returned content is valid and doesn't include invalid bytes
     */
    @Override
    public byte[] getContent() throws IOException, RuntimeException {
        return new byte[0];
    }

    /**
     * TODO: implement (we hebben nog niet afgesproken hoe we dit gaan doen)
     * Saves the given fileContent
     * @param fileContent the content to write away
     * @return 0 if the write is successful, 1 if it was not
     */
    int save(byte[] fileContent) {
        return 0;
    }

    /**
     * @return a clone of ContentHolder, without the reference to it or its fields
     */
    @Override
    public StringHolder clone() {
        return new StringHolder(this.path, this.getLineSeparator().clone(), this.containedText);
    }

    /**
     * @return true if this object equals to the given ContentHolder, false otherwise
     * The objects are equal if:
     * The given contentHolder is of type FileHolder
     * The given contentHolder has the same path as this StringHolder
     * The given contentHolder has the same cointainedText as this StringHolder
     */
    @Override
    public boolean equals(ContentHolder contentHolder) {
        if(contentHolder instanceof StringHolder stringHolder) {
            return this.path.equals(stringHolder.path) &&
                    Arrays.equals(this.getLineSeparator(), stringHolder.getLineSeparator()) &&
                    this.containedText.equals(stringHolder.containedText);
        }
        else{
            return false;
        }
    }
}