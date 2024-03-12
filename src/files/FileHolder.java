package files;

import core.Controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;

/*
 * file aparte klasse omdat eventueel andere io gebruikt moet worden +
 *  andere functie dan buffer
 */
public class FileHolder {
    public static final byte[] lineSeparator = Controller.getLineSeparatorArg() == null ? System.lineSeparator().getBytes() : Controller.getLineSeparatorArg();
    private final String path;
    private File fd;

    /**
     * Creates File object with given path
     */
    public FileHolder(String path) {
        this.fd = new File(path);
        this.path = path;
    }

    String getPath() {
        return new String(this.path);
    }

    byte[] getLineSeparator() {
        return this.lineSeparator == null ? System.lineSeparator().getBytes() : this.lineSeparator;
    }

    /**
     * saves file
     */
    public void save(byte[] fileContent) {
        try {
            Files.write(Path.of(this.path), fileContent);
        } catch (IOException e) {
            System.out.println("[FileHolder] Exception while trying to save file content");
        }
    }

    public void save(String content) {
        try {
            Files.write(Path.of(this.path), content.getBytes());
        } catch (IOException e) {
            System.out.println("[FileHolder] Exception while trying to save file content");
        }
    }

    /**
     * Returns the content of the file
     */
    public final byte[] getContent() {
        try {
            // Check for invalid bytes
            // 1. Non ASCII characters
            byte[] fileContent = Files.readAllBytes(this.fd.toPath());
            for(byte b : fileContent)
                if(b < 32 && b != 10 && b != 13 || 127 <= b)
                    return "Error: Invalid file contents - Invalid bytes".getBytes();
            // 2. Non platform specific line seperators
            byte[] lineSeperatorBytes = System.lineSeparator().getBytes();
            Formatter formatterLine = new Formatter();
            for(byte b : lineSeperatorBytes) formatterLine.format("%02x",b);
            // If line separator was specified, use specified otherwise use System.lineSeparator()
            String lineSeperatorCode = this.lineSeparator == null ? formatterLine.toString() : FileAnalyserUtil.formatBytes(lineSeparator);

            Formatter formatterContent = new Formatter();
            for(byte b : fileContent) formatterContent.format("%02x",b);
            String fileContentFormatted = formatterContent.toString();

            // We zullen enkel voor windows ("0d0a") kijken voor een match.
            // Contains 0d0a, code is 0a
            if((fileContentFormatted.contains("0d0a") && lineSeperatorCode.equals("0a"))
                    // Contains 0a, not 0d0a, code is 0d0a
                    || (fileContentFormatted.contains("0a") && !fileContentFormatted.contains("0d0a") &&
                    lineSeperatorCode.equals("0d0a")))
                return "Error: Invalid file contents - Wrong line separator".getBytes();


            return fileContent;

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("[FileBuffer] Exception while trying to read contents of file.");

        }
        return "".getBytes();
    }

    public FileHolder clone() {
        return new FileHolder(new String(this.path));
    }

    /**
     * Returns true if the {@link FileHolder#path}'s of both objects match.
     */
    public boolean equals(FileHolder holder) {
        return this.path.equals(holder.path);
    }

    public static boolean areContentsEqual(byte[] arr1, byte[] arr2) {
        if(arr1.length != arr2.length) return false;
        for(int i = 0; i < arr1.length; i++)
            if(arr1[i] != arr2[i]) return false;
        return true;
    }

}
