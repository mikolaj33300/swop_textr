package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;

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

    public byte[] getLineSeparator() {
        return lineSeparator;
    }

    public String getPath() {
        return new String(this.path);
    }

    /**
     * saves file
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
     * Returns the content of the file
     */
    public final byte[] getContent() throws IOException, RuntimeException {

            // Check for invalid bytes
            // 1. Non ASCII characters
            byte[] fileContent = Files.readAllBytes(this.fd.toPath());
            for(byte b : fileContent)
                if(b < 32 && b != 10 && b != 13 || 127 <= b)
                    throw new RuntimeException("Error: Invalid file contents - Invalid bytes");
            // 2. Non platform specific line seperators
            byte[] lineSeperatorBytes = System.lineSeparator().getBytes();
            Formatter formatterLine = new Formatter();
            for(byte b : lineSeperatorBytes) formatterLine.format("%02x",b);
            // If line separator was specified, use specified otherwise use System.lineSeparator()
            String lineSeperatorCode = FileAnalyserUtil.formatBytes(System.lineSeparator().getBytes());

            Formatter formatterContent = new Formatter();
            for(byte b : fileContent) formatterContent.format("%02x",b);
            String fileContentFormatted = formatterContent.toString();

            // We zullen enkel voor windows ("0d0a") kijken voor een match.
            // Contains 0d0a, code is 0a
            if((fileContentFormatted.contains("0d0a") && lineSeperatorCode.equals("0a"))
                    // Contains 0a, not 0d0a, code is 0d0a
                    || (fileContentFormatted.contains("0a") && !fileContentFormatted.contains("0d0a") &&
                    lineSeperatorCode.equals("0d0a")))
                throw new RuntimeException("Error: Invalid file contents - Invalid line separator");


            return fileContent;
    }

    /**
     * Return a copy of this FileHolder, without the reference to it
     */
    public FileHolder clone() {
        return new FileHolder(new String(this.path), this.lineSeparator);
    }

    /**
     * Returns true if the {@link FileHolder#path}'s of both objects match.
     */
    public boolean equals(FileHolder holder) {
        return this.path.equals(holder.path);
    }

    /**
     * Checks if given
     */
    public static boolean areContentsEqual(byte[] arr1, byte[] arr2) {
        if(arr1.length != arr2.length) return false;
        for(int i = 0; i < arr1.length; i++)
            if(arr1[i] != arr2[i]) return false;
        return true;
    }

}
