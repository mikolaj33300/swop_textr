package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * file aparte klasse omdat eventueel andere io gebruikt moet worden +
 *  andere functie dan buffer
 */
public class FileHolder {
    private byte[] lineSeparator;
    private final String path;

    private final File fd;

    /**
     * Creates File object with given path
     */
    public FileHolder(String path, byte[] lineSeparator) {
        this.fd = new File(path);
        this.lineSeparator = lineSeparator.clone();
        this.path = path;
    }

    /**
     * @return the line separator used
     */
    public byte[] getLineSeparator() {
        return lineSeparator;
    }

    /**
     * @return the path of the file opened
     */
    public String getPath() {
        return new String(this.path);
    }

    /**
     * saves file
     *
     * @param fileContent the content to write away
     * @return 0 if write is successul, 1 if it was not
     */
    int save(byte[] fileContent) {
        try {
            Files.write(Path.of(this.path), fileContent);
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

    /**
     * Checks if the program should throw an exception when non ascii characters were found
     * @param fileContent the bytes that should be checked for invalid ascii characters
     * @throws RuntimeException when the parameter represented non ascii characters
     */
    protected void checkInvalidCharacters(byte[] fileContent) throws RuntimeException {
        for (byte b : fileContent)
            if (b < 32 && b != 10 && b != 13 || 127 <= b)
                throw new RuntimeException("Error: Invalid file contents - Invalid bytes");
    }

    /**
     * Checks if the parameter content contained byte sequences that were invalid in terms of line separators.
     * Allowed line separators were specified in the constructor {@link FileHolder#lineSeparator}.
     * @param fileContent the bytes that should be checked for a valid line separator
     * @throws RuntimeException when the parameter contained invalid byte sequence
     */
    protected void checkLineSeparator(byte[] fileContent) throws RuntimeException {
        // First get formatted line separator bytes: bytes to 0d0a or 0d
        String lineSeperatorCode = FileAnalyserUtil.formatBytes(lineSeparator);
        String fileContentFormatted = FileAnalyserUtil.formatBytes(fileContent);

        // We zullen enkel voor windows ("0d0a") kijken voor een match.
        // Contains 0d0a, code is 0a
        if ((fileContentFormatted.contains("0d0a") && lineSeperatorCode.equals("0a"))
                // Contains 0a, not 0d0a, code is 0d0a
                || (fileContentFormatted.contains("0a") && !fileContentFormatted.contains("0d0a") &&
                lineSeperatorCode.equals("0d0a")))
            throw new RuntimeException("Error: Invalid file contents - Invalid line separator");

    }

    /**
     * @return the content of the file
     */
    public byte[] getContent() throws IOException {
        byte[] fileContent = Files.readAllBytes(this.fd.toPath());
        checkInvalidCharacters(fileContent);
        checkLineSeparator(fileContent);
        return fileContent;
    }

    /**
     * @return a copy of this {@link FileHolder}, without the reference to it
     */
    public FileHolder clone() {
        return new FileHolder(new String(this.path), this.lineSeparator);
    }

    /**
     * @return true if the {@link FileHolder#path}'s of both objects match.
     */
    public boolean equals(FileHolder holder) {
        return this.path.equals(holder.path);
    }

    /**
     * @param arr1 array 2 to compare
     * @param arr2 array 2 to compare
     * @return if given arrays are equal
     */
    public static boolean areContentsEqual(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++)
            if (arr1[i] != arr2[i]) return false;
        return true;
    }

}
