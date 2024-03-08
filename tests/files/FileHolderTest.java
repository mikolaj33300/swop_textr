package files;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;

public class FileHolderTest {
    FileHolder f1 = new FileHolder("testresources/test.txt");
    FileHolder f1_ = new FileHolder("testresources/test.txt");
    FileHolder f2 = new FileHolder("testresources/test2.txt");

    @Test
    void testGetPath(){
        assertEquals("testresources/test.txt",f1.getPath());
        assertEquals("testresources/test2.txt",f2.getPath());
    }

    @Test
    void testSave(){
        String teststring = "string to test with";
        f1.save(teststring);
        assertEquals(f1.getContent(),teststring);
    }
    @Test
    void testGetContent(){

    }

    @Test
    void testClone(){
        FileHolder f1clone = f1.clone();

        assertNotSame(f1clone, f1);

        assertTrue(f1clone.equals(f1));
        assertTrue(f1.equals(f1clone));
    }

    @Test
    void testEqual(){
        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f1_));
        assertTrue(f1_.equals(f1));
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }
}

/*
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

    private final String path;
    private File fd;
     * Creates File object with given path

    public FileHolder(String path) {
        this.fd = new File(path);
        this.path = path;
    }



     * saves file

    public void save(String fileContent) {

        try {
            FileWriter writer = new FileWriter(this.fd);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("[FileHolder] Exception while trying to save file content");
        }
    }


     * Returns the content of the file
     *
     * @return

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


     * Returns true if the {@link FileHolder#path}'s of both objects match.

    public boolean equals(FileHolder holder) {
        return this.path.equals(holder.path);
    }

}
*/