package files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList; import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// Tests eenkel relevant op mac
public class FileBufferTest {

    @TempDir
    Path path1, path2, path3, path4;
    FileBuffer buffer1, buffer2, buffer3, buffer4;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, "termios is life ;\ntermios is also very useful for terminal apps".getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, "if kaas is\n not a mister\n ; no one is".getBytes());
        path3 = path3.resolve("test3.txt");
        Files.write(path3, "termios".getBytes());
        path4 = path4.resolve("test4.txt");
        Files.write(path4, "hallo kaas i am your loyal student\n i use termios daily".getBytes());
        buffer1 = new FileBuffer(path1.toString(), System.lineSeparator().getBytes());
        buffer2 = new FileBuffer(path2.toString(), System.lineSeparator().getBytes());
        buffer3 = new FileBuffer(path3.toString(), System.lineSeparator().getBytes());
        buffer4 = new FileBuffer(path4.toString(), System.lineSeparator().getBytes());
    }

    @Test
    public void testClose1() {
        assertEquals(buffer1.close(), 0);
    }

    @Test
    public void testClose2() {
        buffer1.write("a".getBytes()[0], 0, 0);
        assertEquals(buffer1.close(), 1);
    }

    @Test
    public void testEquals() {
        assertFalse(buffer1.equals(buffer2));
        assertTrue(buffer1.equals(buffer1.clone()));
        assertFalse(buffer1.equals(new String("a")));
    }

    @Test
    public void testPath() {
        assertEquals(buffer1.getPath(), path1.toFile().getAbsolutePath());
    }

    @Test
    public void testCorrectFileHolder() throws IOException {
        // Test if the FileHolder is equal == Paths are equal
        FileHolder holder = new FileHolder(path1.toString(), System.lineSeparator().getBytes());
        assertTrue(buffer1.getFileHolder().equals(holder));
    }

    @Test
    public void testContentsReadCorrectly() {
        assertTrue(buffer2.contentsEqual(
                new ArrayList<Byte>(Arrays.<Byte>asList(
                        FileAnalyserUtil.wrapEachByteElem("if kaas is\n not a mister\n ; no one is".getBytes())))));
    }

    @Test
    public void testClone() throws IOException {
        assertTrue(buffer1.equals(buffer1.clone()));
        assertNotSame(buffer1, buffer1.clone());
    }

    @Test
    public void testContentsChange() throws IOException {
        buffer1.write("b".getBytes()[0], 0,0);
        assertTrue(FileHolder.areContentsEqual(
            buffer1.getBytes(),
            "btermios is life ;\ntermios is also very useful for terminal apps".getBytes()
        ));
    }

    @Test
    public void testGetsDirty() throws IOException {
        buffer1.write("b".getBytes()[0], 0,0);
        assertTrue(buffer1.getDirty());
    }

    @Test
    public void testSavesCorrectly() throws IOException {
        buffer1.write("b".getBytes()[0], 0,0);
        buffer1.save();
        assertTrue(
                FileHolder.areContentsEqual(
                        Files.readAllBytes(path1),
                        "btermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testNotDirtyAfterSave() {
        buffer1.write("b".getBytes()[0], 0,0);
        assertTrue(buffer1.getDirty());
        buffer1.save();
        assertFalse(buffer1.getDirty());
    }


    @Test
    public void testEnterInsertionPoint1() throws IOException {
        buffer1.enterInsertionPoint(buffer1.convertLineAndColToIndex(0,0));
        assertTrue(
          FileHolder.areContentsEqual(
                   "\ntermios is life ;\ntermios is also very useful for terminal apps".getBytes(),
                  buffer1.getBytes()
          )
        );
    }

    @Test
    public void testEnterInsertionPoint2() throws IOException {
        buffer2.enterInsertionPoint(buffer2.convertLineAndColToIndex(2,0));
        assertTrue(
                FileHolder.areContentsEqual(
                        "if kaas is\n not a mister\n\n ; no one is".getBytes(),
                        buffer2.getBytes()
                )
        );
    }

    @Test
    public void testAmountChar() throws IOException {
        assertEquals(buffer3.getAmountChars(), 7);
    }

    @Test
    public void testDeleteCharacterLine0() throws IOException {
        buffer4.deleteCharacter(1, 0);
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer4.getBytes(),
                        "allo kaas i am your loyal student\n i use termios daily".getBytes()
                        )
                );
    }

    @Test
    public void testDeleteCharacterLine1() throws IOException {
        buffer4.deleteCharacter(1, 1);
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer4.getBytes(),
                        "hallo kaas i am your loyal student\ni use termios daily".getBytes()
                )
        );
    }
// Files.write(path1, "termios is life ;\ntermios is also very useful for terminal apps".getBytes());
    @Test
    public void testWriteCommand() {
        buffer1.writeCmd("t".getBytes()[0], 0, 0);
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "ttermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testBackspaceCommand() {
        buffer1.deleteCharacterCmd(1,0);
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "ermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testUndoWrite() {
        buffer1.writeCmd("t".getBytes()[0], 0, 0);
        buffer1.writeCmd("e".getBytes()[0], 0,1);
        buffer1.undo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "ttermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testUndoDeleteNothing() {
        buffer1.deleteCharacterCmd(0, 0);
        buffer1.undo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "termios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testUndoDeleteCharacter() {
        buffer1.deleteCharacterCmd(1, 0);
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "ermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
        System.out.println(new String(buffer1.getBytes()));
        buffer1.undo();

        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "termios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testRedoWrite() {
        buffer1.writeCmd("t".getBytes()[0], 0, 0);
        buffer1.writeCmd("e".getBytes()[0], 0,1);
        buffer1.undo();
        buffer1.redo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "tetermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testRedoDeleteNothing() {
        buffer1.deleteCharacterCmd(0,0);
        buffer1.undo();
        buffer1.redo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "termios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

    @Test
    public void testRedoDelete() {
        buffer1.deleteCharacterCmd(1,0);
        buffer1.undo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "termios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
        buffer1.redo();
        assertTrue(
                FileHolder.areContentsEqual(
                        buffer1.getBytes(),
                        "ermios is life ;\ntermios is also very useful for terminal apps".getBytes()
                )
        );
    }

}
