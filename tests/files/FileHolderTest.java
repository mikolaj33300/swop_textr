package files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileHolderTest {

    @TempDir
    Path path1, path2;
    FileHolder holder1;
    String content1;

    @BeforeEach
    public void setVariables() throws IOException {
        content1 = "the impact of termios in my life is great and good and i like it";
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, content1.getBytes());
        holder1 = new FileHolder(path1.toString(), System.lineSeparator().getBytes());
    }

    @Test
    public void testGetPath() {
        assertEquals(path1.toFile().getAbsolutePath(), holder1.getPath());
    }

    @Test
    public void testSave() throws IOException {
        holder1.save("are termios users in a cult?".getBytes());
        assertTrue(
                FileHolder.areContentsEqual(
                        "are termios users in a cult?".getBytes(),
                        holder1.getContent()
                )
        );
    }

    @Test
    public void testGetContentBeforeSave() throws IOException {
        assertTrue(
                FileHolder.areContentsEqual(
                        content1.getBytes(),
                        holder1.getContent()
                )
        );
    }

    @Test
    void testClone() {
        assertNotSame(holder1.clone(), holder1);
    }

    @Test
    void testEqual_PerfectCopy_True() {
        FileHolder holder2 = new FileHolder(path1.toString(), System.lineSeparator().getBytes());
        assertTrue(holder1.equals(holder2));
    }

    @Test
    void testEqual_DifferentObject_False() {
        assertFalse(holder1.equals(new String("certified termios enthusiast")));
    }

    @Test
    public void testEqual_DifferentLineSeparatorSameString_True() {
        FileHolder holder2 = new FileHolder(path1.toString(), " ".getBytes());
        assertTrue(holder1.equals(holder2));
    }

    @Test
    public void testEqual_DifferentPath_DifferentContent_False() {
        FileHolder holder2 = new FileHolder("hellomiste", " ".getBytes());
        assertFalse(holder1.equals(holder2));
    }

}

