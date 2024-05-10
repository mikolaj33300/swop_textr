package files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterHelper {

    /**
     * Helper method that writes given text into the file at given path
     * @param path the path to the File
     * @param text the text to be written
     */
    public static void write(String path, String text) {
        try {
            // Overwrite file test.txt
            FileWriter writer = new FileWriter(new File(path));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
