package files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * file aparte klasse omdat eventueel andere io gebruikt moet worden +
 *  andere functie dan buffer
 */
public class FileHolder {
    private final String path;
    private File fd;

    /**
     * Creates File object with given path
     */
    public FileHolder(String path) {
        this.fd = new File(path);
        this.path = path;
    }

    public String getPath() {
        return new String(this.path);
    }

    /**
     * saves file
     */
    public void save(String fileContent) {

        try {
            FileWriter writer = new FileWriter(this.fd);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("[FileHolder] Exception while trying to save file content");
        }
    }

    /**
     * Returns the content of the file
     *
     * @return
     */
    public final String getContent() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.fd));
            String contents = "";
            String line;

            while ((line = reader.readLine()) != null)
                contents += line;

            return contents;

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("[FileBuffer] Exception while trying to read contents of file.");

        }
        return "";
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

}
