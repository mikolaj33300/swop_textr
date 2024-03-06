package files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Suggestie om File en FileBuffer samen te voegen.
 *
 * Onze File klasse zou een java IO File bevatten...
 *
 * Dit is enkel een suggestie je kan dit makkelijk verwijderen.
 * Voor de zekerheid heb ik de andere klassen geïmplementeerd volgens het schema
 * Ook dat is maar een suggestie en mogen volledig herschreven worden naar eigen
 * wil.
 */
public class FileBuffer2 {

    private File file;
    private boolean dirty = false;
    private String content;

    /**
     * Creates FileBuffer object with given path;
     * @param path
     */
    public FileBuffer2(String path) {
        Path checkPath = Paths.get(path);
        if(!Files.exists(checkPath)) {}
        this.file = new File(path);
        this.content = getContent();
    }

    /**
     * Returns the content of the file
     * @return
     */
    public final String getContent() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String contents = "";
            String line;

            while((line = reader.readLine()) != null)
                contents += line;

            return contents;

        } catch(IOException e) {

            e.printStackTrace();
            System.out.println("[FileBuffer] Exception while trying to read contents of file.");

        }
        return "";
    }

    /**
     * Updates the content of the FileBuffer
     */
    public final void update(String updatedContents) {
        this.content = updatedContents;
        dirty = true;
    }

    /**
     * Saves the buffer contents to disk
     */
    public final void save() {
        if(!dirty) return;
        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(this.content);
            writer.close();
            this.dirty = false;
        } catch(IOException e) {
            System.out.println("[FileBuffer] Exception while trying to save file contents");
        }
    }

}
