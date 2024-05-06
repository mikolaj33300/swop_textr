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
public class FileHolder extends ContentsHolder{
    private final File fd;

    /**
     * Creates File object with given path
     */
    public FileHolder(String path, byte[] lineSeparator) {
        super(path, lineSeparator.clone());
        this.fd = new File(path);
    }

    /**
     * saves file
     * @param fileContent the content to write away
     * @return 0 if the write is successul, 1 if it was not
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
     * @return the content of the file
     */
    public final byte[] getContent() throws IOException, RuntimeException {

            // Check for invalid bytes
            // 1. Non ASCII characters
            byte[] fileContent = Files.readAllBytes(this.fd.toPath());
            for(byte b : fileContent)
                if(b < 32 && b != 10 && b != 13 || 127 <= b)
                    throw new RuntimeException("Error: Invalid file contents - Invalid bytes");
            // 2. Non platform specific line seperators
            byte[] lineSeperatorBytes = lineSeparator;
            Formatter formatterLine = new Formatter();
            for(byte b : lineSeperatorBytes) formatterLine.format("%02x",b);
            // If line separator was specified, use specified otherwise use System.lineSeparator()
            String lineSeperatorCode = FileAnalyserUtil.formatBytes(lineSeparator);

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
     * @return a copy of this FileHolder, without the reference to it
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
}
